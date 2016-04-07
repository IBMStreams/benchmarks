// LDD.cpp : Defines the entry point for the console application.
//
#include <string>
#include <iostream>

#include <time.h>

#include "LRDataProvider.h"

using namespace std;

void ErrorHandler(int nErrorCode)
{
	switch(nErrorCode)
	{
		case END_OF_FILE:
			{
				cout << "End of data file. Enter Q to quit." << endl;
			}
			break;
		case ERROR_FILE_NOT_FOUND:
			{	
				cout << "Data file not found. Check data file path name." << endl;
			}
			break;
		case ERROR_INVALID_FILE:
			{
				cout << "Invalid file handler. Restart the system." << endl;
			}
			break;
		case ERROR_BUFFER_OVERFLOW:
			{
				cout << "Buffer over flow. Increase the buffer size." << endl;
			}
			break;
		default:
			{
				cout << "Programming error." << endl;
			}
			break;
	}
}

//pthread_mutex_t  mutex_lock = PTHREAD_MUTEX_INITIALIZER;

int main(int argc, char* argv[])
{
	pthread_mutex_t  mutex_lock = PTHREAD_MUTEX_INITIALIZER;
	//char* dataFile = "D:\\Nga\\Projetcs\\LinearRoad\\Datadriver\\test\\linear\\xway0-withaccidents.dat";
	//char* dataFile = "D:\\Nga\\Projetcs\\LinearRoad\\Datadriver\\test\\linear\\infile2.dat";
	char* dataFile = "/home/nga/Projects/linearroad/code/code3/infile1.dat";
	//char* dataFile = "/home/nga/Projects/linearroad/code/code3/infile2.dat";

	//Test - Initialize a tuple 
	/*
	LPTuple lpTuple = new LRTuple();
	cout << lpTuple->ToString() << endl;

	delete lpTuple;
	*/

	//Test - CLRDataProvider
	CLRDataProvider* provider = new CLRDataProvider();
	
	//Initialize the provider
	cout << "Initializing..." << endl;
	int ret = provider->Initialize(dataFile, 10000, &mutex_lock);

	//provider->m_lpBuffer->m_lock = &mutex_lock;

	//Allocate caller's buffer
	if ( ret == SUCCESS )
	{
		//Using the provider
		if( provider->PrepareData(provider) == SUCCESS )
		{
			int		nTuplesRead = 0;
			int		nMaxTuples  = 100;
			LPTuple lpTuples = new LRTuple[nMaxTuples ];

    		for(;;)
			{
				//sleep(1);
				//Simulate sleep()
				char c;
				cout << "Press any key to continue...";
				cin >> c;

				if( c == 'Q' )
				{
					break;
				}

				for(;;)
				{
					//Gets available data
					int ret = provider->GetData(lpTuples, nMaxTuples, nTuplesRead);

					if ( ret < 0 )
					{
						//Handle erros including eof
						ErrorHandler(ret);
						break;
					}

					if ( nTuplesRead == 0 )
					{
						//No tuple available
						break;
					}
					
					//Using the return data
					for(int i = 0; i < nTuplesRead; i++ )
					{
						cout << lpTuples[i].ToString();
					}

					if ( nTuplesRead < nMaxTuples ) 
					{
						//Last tuple has been read
						break;
					}
				}
			}
		}
		
		//Uninitialize the provider
		cout << "Uninitialize..." << endl;
		provider->Uninitialize();
	}
	else
	{
		cout << "Cannot initilize the provider." << endl;
	}

	delete provider;
	
	return 0;
}

