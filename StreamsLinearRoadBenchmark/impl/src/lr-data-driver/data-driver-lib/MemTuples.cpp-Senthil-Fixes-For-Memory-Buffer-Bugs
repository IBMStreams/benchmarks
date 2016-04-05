
#include <string>
#include <iostream>
#include <pthread.h>	
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "LRDataProvider.h"

using namespace std;

CMemTuples::CMemTuples(int nBufferSize, int nMaxLineSize)
{
	m_nBufferSize	= nBufferSize;
	m_nMaxLineSize	= nMaxLineSize;

	m_lpzBuf		= new char[DEFAULT_MAX_LINE_SIZE];
	m_lpTuples		= new LRTuple[m_nBufferSize];
	//m_lpTuples	= malloc((size_t)(m_nBufferSize*sizeof(LRTuple));

	m_nFirstTuple		= -1;
	m_nCurrentTuple		= -1;
	m_nLastTuple		= -1; 
	m_nTuplesInBuffer	= 0; 
	m_nAvailableTuples	= 0;
	m_bIsEof			= false;

}

CMemTuples::~CMemTuples(void)
{
	if ( m_lpzBuf != NULL )
	{
		delete m_lpzBuf;
	}
	if ( m_lpTuples != NULL )
	{
		delete[] m_lpTuples;
	}
}

// Convert the buffer content to string
char* CMemTuples::ToString(void)
{
	char* str = new char[m_nBufferSize*m_nMaxLineSize + 1];
	memset(str, '\0', m_nBufferSize*m_nMaxLineSize + 1);

	if ( (m_nFirstTuple == -1) || (m_nLastTuple == -1) )
	{
		return str;		
	}
	
	for(int i = m_nFirstTuple; i <= m_nLastTuple; i++ )
	{
		char* temp = m_lpTuples[i].ToString();
		str = strcat(str, temp);
	}

	return str;
}

// Fill tuples until the buffer is full or eof is reached.
int CMemTuples::FillTuples(ifstream* file)
{
	pthread_mutex_lock(m_lock);

	//Check if buffer is full
	if ( m_nTuplesInBuffer == m_nBufferSize )
	{
		pthread_mutex_unlock(m_lock);
		return SUCCESS;
	}

	//Check for valid file hanler
	if ( file == NULL )
	{
		pthread_mutex_unlock(m_lock);
		return ERROR_INVALID_FILE;
	}
	
	while ( !(m_bIsEof = file->eof()) )
	{
		//Advance one slot
		if ( ++m_nLastTuple == m_nBufferSize )
		{
			m_nLastTuple = 0; 
		}
				
		//Gets data into the buffer
		file->getline(m_lpzBuf, sizeof(char[DEFAULT_MAX_LINE_SIZE]) );
		//Initialize tuple with string
		m_lpTuples[m_nLastTuple].Initialize(m_lpzBuf);

		if ( m_nFirstTuple == -1 )
		{
			m_nFirstTuple = 0;
		}
		
		//Check for buffer is full
		++m_nTuplesInBuffer;

		if (m_nTuplesInBuffer  == m_nBufferSize )
		{
			break;
		}
	}

	pthread_mutex_unlock(m_lock);

	return SUCCESS;
}

int CMemTuples::GetTuples(LPTuple tuples, int nMaxTuples, int& nTuplesRet)
{
	pthread_mutex_lock(m_lock);
	
	//Number of return tuples
	nTuplesRet = 0;

	//Check for available of tuples 
	//Check for number of tuples in buffer 
	//Check for available tuples in buffer 
	if ( (m_nAvailableTuples == 0) || (m_nTuplesInBuffer == 0) || (m_nCurrentTuple == -1) )
	{
		if ( (m_nTuplesInBuffer == 0) && m_bIsEof )
		{
			pthread_mutex_unlock(m_lock);
			return END_OF_FILE;
		}
		pthread_mutex_unlock(m_lock);
		return nTuplesRet;
	}

	
	//Calculate number of return tuples 
	nTuplesRet = m_nAvailableTuples;

	//Number of tuples return should not bigger than nMaxTuple
	nTuplesRet = (nTuplesRet > nMaxTuples) ? nMaxTuples : nTuplesRet;
		
	if ( nTuplesRet == 0 )
	{
		pthread_mutex_unlock(m_lock);
		return (nTuplesRet);
	}

	if ( (m_nCurrentTuple >= m_nFirstTuple) )
	{
		memcpy(&tuples[0], &m_lpTuples[m_nFirstTuple], (size_t)(sizeof(LRTuple)*nTuplesRet));
        m_nFirstTuple += nTuplesRet;
	}
	else
	{
		if ( nTuplesRet < (m_nBufferSize - m_nFirstTuple) )
		{
			 memcpy(&tuples[0], &m_lpTuples[m_nFirstTuple], (size_t)(sizeof(LRTuple)*(nTuplesRet)));
			 m_nFirstTuple += nTuplesRet;
		}
		else
		{
			 int nFirstRead = (m_nBufferSize - m_nFirstTuple);
			 memcpy(&tuples[0], &m_lpTuples[m_nFirstTuple], (size_t)(sizeof(LRTuple)*(nFirstRead)));
			 m_nFirstTuple = 0;

			 if ( (nTuplesRet - nFirstRead) > 0 )
			 {
				memcpy(&tuples[nFirstRead], &m_lpTuples[m_nFirstTuple], (size_t)(sizeof(LRTuple)*(nTuplesRet - nFirstRead)));
				///// Senthil from IBM added the following line on Aug/27/2015.
				///// Code before this change left the m_nFirstTuple at index 0. That is not correct and
				///// that will cause undesired side effects by sending extra events in an out of order fashion.
				///// Let us fix it by assigning a new value to the m_nFirstTuple variable.
				/////
				m_nFirstTuple = (nTuplesRet - nFirstRead);
			 }
		}
	}
	m_nTuplesInBuffer -= nTuplesRet;
	m_nAvailableTuples -= nTuplesRet;

	pthread_mutex_unlock(m_lock);

	return (nTuplesRet); 
}

//Make buffer availbe to ts seconds
void CMemTuples::AdvanceTo(int ts)
{
	pthread_mutex_lock(m_lock);
	
	if ( m_nTuplesInBuffer == 0 )
	{
		pthread_mutex_unlock(m_lock);
		return;
	}

	///// Senthil from IBM added the following if block on Aug/28/2015 to 
	///// take care of the condition when we will wrap around from the
	///// end of the circular buffer back to the beginning of the circular buffer.
	///// If we exited the following for loop previously just after checking the 
	///// final slot of the circular buffer, let us prepare to start from index 0 now.
	if (m_nCurrentTuple >= m_nBufferSize - 1) {
           m_nCurrentTuple = -1;
        }

	for( ; m_nAvailableTuples < m_nTuplesInBuffer  ; )
	{
		if (m_lpTuples[m_nCurrentTuple+1].m_iTime > ts + 1 )
		{
			break;
		}
		++m_nCurrentTuple;
		++m_nAvailableTuples;

                ///// Senthil from IBM commented the following line on Aug/28/2015 and
                ///// added a new line below this commented line.
                ///// Check should be done for wrapping around the circular buffer at the
                ///// very last element instead of reaching at the buffer size which is out of bounds.
                ///// Last element is going to be m_nBufferSize -1 instead of m_nBufferSize since
                ///// this array is based on zero based indexing.
                /////
		///// if ( m_nCurrentTuple == m_nBufferSize )
		/////
		if ( m_nCurrentTuple >= m_nBufferSize - 1 )
		{
			///// Senthil from IBM commented the following line on Aug/27/2015 and
			///// added a new line below this commented line.
			///// When we reach the end of the buffer, we should set the 
			///// current tuple to -1 so that we will get a chance to check the
			///// tuple in index 0 at the top of this for loop. Old (commented) line below
			///// was setting it to 0 which will skip the index 0 that may cause
			///// unnecessary side effects when we do a wrap around in this circular buffer.
			/////
			///// m_nCurrentTuple = 0;
			/////
			///// During the wrap around of the circular buffer, set it to -1 to so that
			///// we will start checking from index 0 at the top of this for loop.
			m_nCurrentTuple = -1;
		}
	}

	///// Senthil from IBM added this line on Aug/28/2015.
	///// If we dropped out of the for loop just before checking the index 0, 
	///// current tuple will be pointing to -1. We can't leave it at -1.
	///// In that case, we must set it to the very last entry in the circular buffer.
	if (m_nCurrentTuple == -1) {
		m_nCurrentTuple = m_nBufferSize - 1;
	} 

	pthread_mutex_unlock(m_lock);
}

