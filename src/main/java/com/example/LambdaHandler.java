package com.example;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.log4j.*;


public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
  private SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
  private Logger log = Logger.getLogger(LambdaHandler.class);

  public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
    if (handler == null) {
      try {
        handler = SpringLambdaContainerHandler.getAwsProxyHandler(Application.class);
      } catch (ContainerInitializationException e) {
        log.error("Cannot initialize spring handler", e);
        return null;
      }
    }
    return handler.proxy(awsProxyRequest, context);
  }
}
