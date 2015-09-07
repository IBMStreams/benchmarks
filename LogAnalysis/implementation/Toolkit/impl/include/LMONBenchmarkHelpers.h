/* begin_generated_IBM_copyright_prolog                             */
/*                                                                  */
/* This is an automatically generated copyright prolog.             */
/* After initializing,  DO NOT MODIFY OR MOVE                       */
/* **************************************************************** */
/* Licensed Materials - Property of IBM                             */
/* 5724-Y95                                                         */
/* (C) Copyright IBM Corp.  2014, 2015    All Rights Reserved.      */
/* US Government Users Restricted Rights - Use, duplication or      */
/* disclosure restricted by GSA ADP Schedule Contract with          */
/* IBM Corp.                                                        */
/*                                                                  */
/* end_generated_IBM_copyright_prolog                               */
#include <sched.h>
#include <sys/types.h>
#include <iostream>

#ifdef PINNUMA
#include <numa.h>
#endif


namespace lmon {
    namespace benchmark {
      inline int32_t pinToCpu(int32_t cpu)
      {
	    if(cpu < 0) {
              //std::cout << "ZZZ NO PIN" << std::endl;
	      return 0;
            }
	    
#ifndef PINNUMA 	
            cpu_set_t cpuSet;
            CPU_ZERO(&cpuSet); 

	    CPU_SET(cpu, &cpuSet);
            //pid_t pid = gettid();
            //TOM
            pid_t pid = getpid();
            //std::cout << "ZZZ Pinning to cpu " << cpu << std::endl;
            return sched_setaffinity(pid, sizeof(cpu_set_t), &cpuSet);
#else
            // This will pin to all CPUs on the numa
            
            // First make sure numa APIs are working
            if (-1 == numa_available()) 
              return(0);

            // How many numa nodes on this system?
            int numNN = numa_max_node() + 1;

            // Which numa node to run this process
            // on is based on the cpu value passed in
            int nNode = cpu/2 % numNN;
            //std::cout << "ZZZ Running cpu " << cpu << " on numa node " << nNode << std::endl;

            // Get all CPUs in this numa node and add it to the CPU_SET
            cpu_set_t cpuSet;
            CPU_ZERO(&cpuSet); 
            struct bitmask *nMask = numa_allocate_cpumask();
            numa_node_to_cpus(nNode, nMask);
            unsigned int nbits = 8 * numa_bitmask_nbytes(nMask);
            for(int i=0; i < nbits; i++) {
              if(numa_bitmask_isbitset(nMask, i)) {
                //std::cout << "ZZZ Adding CPU " << i << " to the cpuset." << std::endl;
	        CPU_SET(i, &cpuSet);
              }
            }
            numa_free_cpumask(nMask);
            //pid_t pid = gettid();
            //TOM
            pid_t pid = getpid();
            int int_rc = sched_setaffinity(pid, sizeof(cpu_set_t), &cpuSet);
            std::cout << "ZZZ Running DS: " << cpu << " pid: " << pid << " on numa node: " << nNode << " rc: " << int_rc  << std::endl;
	    return int_rc;

#endif
	    
      }
        template <typename T>
        void readValue(SPL::rstring & token, T & value)
        {
            std::istringstream istr(token);
            istr >> value;
        }

	// TOM modified
        template <>
        inline void readValue<SPL::uint8>(SPL::rstring & token, SPL::uint8 & value)
        {
            value = atoi(token.c_str());
        }

        template <>
        inline void readValue<SPL::rstring>(SPL::rstring & token, SPL::rstring & value)
        {
            value = token;
        }

        template <>
        inline void readValue<uint8_t>(SPL::rstring & token, uint8_t & value)
        {
            value = atoi(token.c_str());
        }

        template <>
        inline void readValue<uint16_t>(SPL::rstring & token, uint16_t & value)
        {
            value = atoi(token.c_str());
        }

        template <>
        inline void readValue<uint32_t>(SPL::rstring & token, uint32_t & value)
        {
            value = atoi(token.c_str());
        }

        template <>
        inline void readValue<uint64_t>(SPL::rstring & token, uint64_t & value)
        {
            value = atoll(token.c_str());
        }

        template<typename T>
        inline void parseEvent_Native(T & event, SPL::rstring const & text, int32_t thisYear) 
        {
            struct tm brokenDownTime;
            char const * event_cstr = text.c_str();
            
            // 10/26 03:04:21.036 B1 : EVENT=Msg_Rcvd, E_ID=9, D_ID=D1, M_ID=M1, R=78943513
	    SPL::rstring token = "";
            int32_t n, m;
            {
                static __thread char lastTime[20];
                static __thread time_t lastTimeT = -1;
                time_t time = 0;   
                if (lastTimeT!=-1 &&
                    strncmp (lastTime, event_cstr, n=strlen(lastTime)) == 0) {
                        time = lastTimeT;                    
                } else {
                    const char *p = strptime(event_cstr, "%m/%d %H:%M:%S.", &brokenDownTime);
                    assert(p!=NULL);
                    brokenDownTime.tm_year = thisYear;
		    brokenDownTime.tm_isdst = 0;
                    brokenDownTime.tm_zone = "UTC";

                    time = mktime(&brokenDownTime);
                    assert(time!=-1);
                    time += brokenDownTime.tm_gmtoff;
                    n = p - event_cstr;
                    strncpy (lastTime, event_cstr, n);
                    lastTime[n] = '\0';
                    lastTimeT = time;
                }
                m = text.find(" ", n);                
                token = text.substr(n, m-n);
                uint32_t msecs;
                readValue(token, msecs);
                event.get_timeMillis() = time*1000 + msecs;
            }
            {
                n = m + 1;
                token = text.substr(n, 1);
                if(token=="A")
                    event.get_sourceKind() = T::sourceKind_type::A;
                else
                    event.get_sourceKind() = T::sourceKind_type::B;
                // avoid IO streams
                // readValue(token,  event.get_sourceKind());
            }
            {
                n += 1;
                m = text.find(" ", n);
                token = text.substr(n, m-n);
                readValue(token,  event.get_sourceIndex());
            }
            {
                n = m + 9;
                m = text.find(",", n);
                if(m==-1) 
                    m = text.size();
                token = text.substr(n, m-n);
                if(token=="Msg_Sent")
                    event.get_eventKind() = T::eventKind_type::Msg_Sent;
                else if(token=="Msg_Rcvd")
                    event.get_eventKind() = T::eventKind_type::Msg_Rcvd;
                else 
                    event.get_eventKind() = T::eventKind_type::Filler;
                // avoid IO streams
                // readValue(token,  event.get_eventKind());
            }
            if(token != "Filler") {
                {
                    n = m + 7;
                    m = text.find(",", n);
                    token = text.substr(n, m-n);
                    readValue(token,  event.get_eventId());
                }
                
                {
                    n = m + 7;
                    m = text.find(",", n);
                    token = text.substr(n, m-n);
                    readValue(token,  event.get_deviceId());
                }
                
                {
                    n = m + 7;
                    m = text.find(",", n);            
                    token = text.substr(n, m-n);
                    readValue(token,  event.get_messageId());
                }
                
                {
                    n = m+4;
                    token = text.substr(n, text.size()-n);
                    readValue(token,  event.get_randomId());
                }
            }
        }

        template<typename T>
        inline void formatEvent_Native(SPL::rstring & text, T const & event) 
        {
            uint64_t timeMillis = event.get_timeMillis();
            time_t timeSecs = timeMillis / 1000;
            uint32_t timeMillisRemains = timeMillis % 1000;

	    // TOM modified
            static std::ostringstream ostr;  
            {
                static __thread char lastTime[20];
                static __thread time_t lastTimeT = -1;
                
                if (lastTimeT != timeSecs) {
                    struct tm brokenDownTime;
                    struct tm * tres = gmtime_r(&timeSecs, &brokenDownTime);
                    assert(tres!=NULL);
                    lastTimeT = timeSecs;
                    strftime(lastTime, sizeof(lastTime), "%m/%d %H:%M:%S.", &brokenDownTime);
                }
		
                ostr << lastTime;
            }

            ostr << timeMillisRemains << " " 
                 << event.get_sourceKind().getValue()
                 << event.get_sourceIndex()
                 <<" : EVENT=" << event.get_eventKind().getValue();            
            if (event.get_eventKind().getValue() != "Filler") {
                ostr << ", E_ID=" << event.get_eventId()
                     << ", D_ID=" << event.get_deviceId()
                     << ", M_ID=" << event.get_messageId()
                     << ", R=" << event.get_randomId();  
            }

            text = ostr.str();
	    // TOM modified
	    ostr.str(std::string());
            ostr.clear();
       }
    }
}

