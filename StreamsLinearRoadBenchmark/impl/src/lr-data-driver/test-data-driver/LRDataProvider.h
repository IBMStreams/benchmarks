
#ifndef __CLRDATAPROVIDER_H__
#define __CLRDATAPROVIDER_H__

#include <string>
#include <iostream>
#include <fstream>

#include <pthread.h>	

using namespace std;

#define SUCCESS			 0
#define END_OF_FILE            	-1 

#define ERROR_FILE_NOT_FOUND	-2
#define ERROR_INVALID_FILE	-3
#define ERROR_BUFFER_OVERFLOW	-4


#define DEFAULT_BUFFER_SIZE	10000	// number of tuples
#define DEFAULT_MAX_LINE_SIZE	500		// maximun number of characters per line
#define DEFAULT_DELIMS			","	

struct LRTuple
{
	int	m_iType;   // Type:
				   //	. 0: position report
				   //	. 2: account balance request
				   //	. 3: daily expenditure request
				   //	. 4: travel time request			 
	int	m_iTime;   // 0...10799 (second), timestamp position report emitted
	int	m_iVid;	   // 0...MAXINT, vehicle identifier
	int	m_iSpeed;  // 0...100, speed of the vehicle
	int	m_iXway;   // 0...L-1, express way
	int	m_iLane;   // 0...4, lane
	int	m_iDir;    // 0..1, direction
	int	m_iSeg;    // 0...99, segment
	int	m_iPos;    // 0...527999, position of the vehicle
	int	m_iQid;    // query identifier
	int m_iSinit;  // start segment
	int	m_iSend;   // end segment
	int	m_iDow;    // 1..7, day of week
	int	m_iTod;    // 1...1440, minute number in the day
	int	m_iDay;    // 1..69, 1: yesterday, 69: 10 weeks ago

	LRTuple();
	LRTuple(char* str);
	LRTuple(char* str, char* delims);
	
	void Initialize();
	void Initialize(char* str);
	void Initialize(char* str, char* delims);

	char* ToString();
	
};

#define LPTuple LRTuple*

class CMemTuples
{

public:
	CMemTuples(int nBufferSize, int nMaxLineSize);
	virtual ~CMemTuples(void);
	
    // Convert the buffer content to string
	char* ToString(void);

protected:

	// Array of tuples
	LPTuple m_lpTuples;
	
	//One line of raw data
	char*   m_lpzBuf;

	// Max number of tuples.
	int m_nBufferSize;
	// Max number of character in each line of data.
	int m_nMaxLineSize;
	
	// The first tuple's index
	int m_nFirstTuple;
	// The last tuple's index
	int m_nLastTuple;
	// The tuple's index at current time
	int m_nCurrentTuple;
	// The number of tuples in buffer 
	int m_nTuplesInBuffer;
	// The number of available tuples ready to get
	int m_nAvailableTuples;
	//True if eof file, false otherwise.
	bool m_bIsEof;

public:
	
	 pthread_mutex_t*  m_lock; 

public:

	// Read and fill tuples which have time < nTs 
	int FillTuples(ifstream* file);
	
	// Gets current tuples available in the buffer 		
	int GetTuples(LPTuple tuples, int nMaxTuples, int& nTuplesRet);

	//Make buffer available to ts seconds
	void AdvanceTo(int ts);


};


class CLRDataProvider
{
public:
	
	// Constructs a new instance of CLRDataProvider
	CLRDataProvider(void);

	// Release recourses
	virtual ~CLRDataProvider(void);

	// Initialize the provider with a data file name and buffer size
	int Initialize(char* dataFileName, pthread_mutex_t*  mutex_lock);

	// Initialize the provider with a data file name and buffer size
	int Initialize(char* dataFileName, int nBufferSize, pthread_mutex_t*  mutex_lock);

	// Gets data currently in the buffer
	int GetData(LPTuple lpTuples, int nMaxTuples, int& nReadTuples);

	// Release resources
	int Uninitialize(void);

	// Start the clock (stick)
	int PrepareData(CLRDataProvider* provider);

	// Gets string of current data in the buffer, for debugging
	char* ToString(void);

	
private:

	// Input file stream
	ifstream*	m_ifstream;
	
	// Last system time
	time_t		timeSys; 

	// Last get data time 
	time_t		timeLast; 

protected:

	// Buffer of tuples in memory
	CMemTuples* m_lpBuffer;
		
	//True if uninitializing, false otherwise.
	bool	m_bIsUninitializing;

	// Background process thread
	pthread_t m_workerThread;

public:

	static void* Process( void *ptr );

};

#endif
