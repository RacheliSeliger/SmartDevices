# SmartDevices

## Overview 

The Generic IoT Infrastructure project provides users with a comprehensive infrastructure for efficiently managing, monitoring, and analyzing their IoT data. 
It offers a scalable and flexible solution that incorporates various design patterns, including Factory, Singleton, and Observer, along with robust object-oriented programming principles.

By integrating these components, the infrastructure empowers users to effortlessly manage their IoT ecosystem. It provides a seamless flow of data through the gateway server, efficient request handling via the request handler, and dynamic command execution using the command factory. The thread pool ensures parallel processing, enhancing overall performance and scalability

## How to Install and Run the Project

To install and run the Generic IoT Infrastructure project, follow these steps:

    1. *Set up a Tomcat Server* : Install and configure a Tomcat server on your machine. Ensure it is properly set up to run Java web applications.

    2. Deploy the Project: Deploy the project onto the Tomcat server. This typically involves copying the project files to the appropriate directory within the Tomcat installation.

    3. Configure the Gateway Server: Open the GateWay class and configure it to listen for incoming HTTP requests. Specify the desired endpoints and ensure they are correctly mapped to the corresponding functionality within the project.

    4. Start the Server: Start the Tomcat server and wait for it to initialize.

    5. Send HTTP Requests: Use a tool such as cURL or a web browser to send HTTP requests to the appropriate endpoints. Include the relevant data and company information in the request payload. Use the appropriate command for your request, such as "create company," "create product," "IoT register," or "IoT update."

    6. Handle the Response: The infrastructure will process the request based on the executed commands and generate a response. Handle the response according to your requirements, whether it's displaying the response in the browser or programmatically processing it..

