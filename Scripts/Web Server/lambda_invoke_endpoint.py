import os
import io
import boto3
import json
import csv

# grab environment variables
ENDPOINT_NAME = os.environ['ENDPOINT_NAME']
runtime= boto3.client('runtime.sagemaker')

def lambda_handler(event, context):
    print("Received event: " + json.dumps(event, indent=2))
    
    data = json.loads(json.dumps(event))
    payload = bytes(data['data'], 'utf-8')
    
    # return payload
    
    response = runtime.invoke_endpoint(EndpointName=ENDPOINT_NAME,
                                      Body=payload)
    print(response)
    result = json.loads(response['Body'].read().decode())
    print(result)
    pred = result['outputs']['score']['floatVal']
    # predicted_label = pred
    
    return pred