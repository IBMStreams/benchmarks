#include <time.h>
#include <pthread.h>

#include "LRDataProvider.h"

CLRDataProvider::CLRDataProvider(void)
{
	m_lpBuffer = NULL; 
	m_ifstream = NULL;
	m_bIsUninitializing = false;
}

CLRDataProvider::~CLRDataProvider(void)
{
}

int CLRDataProvider::Initialize(char* lpzDataFileName, pthread_mutex_t*  mutex_lock)
{
	return Initialize(lpzDataFileName, DEFAULT_BUFFER_SIZE, mutex_lock);
}


// Initialize the provider with a data file name and buffer size
int CLRDataProvider::Initialize(char* lpzDataFileName, int nBufferSize, pthread_mutex_t*  mutex_lock)
{
	int ret = 0;

	// Open input file for reading
	m_ifstream = new ifstream();
	
	try
	{	  
	     ifstream fin(lpzDataFileName, ios::in);
	     if (!fin)
	     {
	       return  ERROR_FILE_NOT_FOUND;
	     }
	     fin.close();

	     m_ifstream->open(lpzDataFileName, ios::in);

	}
	catch(std::exception&)
	{
	  return ERROR_FILE_NOT_FOUND;
	}

	m_lpBuffer = new CMemTuples(nBufferSize, DEFAULT_MAX_LINE_SIZE);
	m_lpBuffer->m_lock = mutex_lock;

	//Everything is OK
	return ret;
}

int CLRDataProvider::Uninitialize(void)
{
	//Set uninitialize flag
	m_bIsUninitializing = true;

	pthread_join(m_workerThread, NULL); 

	if ( m_lpBuffer != NULL )
	{
		delete m_lpBuffer;
	}

	if ( m_ifstream != NULL )
	{
		m_ifstream->close();
		delete m_ifstream;
	}

	return 0;
}


// Start the clock (stick)
int CLRDataProvider::PrepareData(CLRDataProvider* provider)
{
	//This part will be in worker thread
	time(&timeSys); 
	
	//Create worker thread
	pthread_create(&m_workerThread, NULL, CLRDataProvider::Process, (void*)provider);

	return SUCCESS;
}

void* CLRDataProvider::Process(void* ptr)
{
	CLRDataProvider* provider = (CLRDataProvider*)ptr;
	
	while ( !provider->m_bIsUninitializing )
	{
		//Fill buffer with tuples
		provider->m_lpBuffer->FillTuples(provider->m_ifstream);

		//Get the system time
		time(&provider->timeLast); 
		//Calculate the elapse time in second
		int ts = (int)difftime(provider->timeLast, provider->timeSys);
		//Make data available for ts seconds
		provider->m_lpBuffer->AdvanceTo(ts);

		//Yield other thread
		sleep(1);
	}
	
	return (NULL);
}

// Gets data currently in the buffer
int CLRDataProvider::GetData(LPTuple lpTuples, int nMaxTuples, int& nReadTuples)
{
	int ret = SUCCESS;

	//Reset the number of tuples read
	nReadTuples = 0;

	//Gets data from the buffer
	ret = m_lpBuffer->GetTuples(lpTuples, nMaxTuples, nReadTuples);

	return ret;
}


// Gets string of current data in the buffer
char* CLRDataProvider::ToString(void)
{
	if ( m_lpBuffer != NULL )
	{
		return m_lpBuffer->ToString();
	}
	return NULL;
}

