# SmartDevices

## Overview 

The Generic IoT Infrastructure project provides users with a comprehensive infrastructure for efficiently managing, monitoring, and analyzing their IoT data. 
It offers a scalable and flexible solution that incorporates various design patterns, including Factory, Singleton, and Observer, along with robust object-oriented programming principles.

By integrating these components, the infrastructure empowers users to effortlessly manage their IoT ecosystem. It provides a seamless flow of data through the gateway server, efficient request handling via the request handler, and dynamic command execution using the command factory. The thread pool ensures parallel processing, enhancing overall performance and scalability

## Features
The Generic IoT Infrastructure offers a range of features to facilitate efficient IoT operations and data management:
1. **Company and Product Registration**: Companies can easily register themselves and their products within the platform. The registration details are securely stored in a MySQL database, allowing for seamless management of registered entities.
2. **User Registration and Updates** : Customers who purchase IoT products from registered companies can register with their individual products. They receive timely updates and notifications regarding their products, which are stored by the platform in MongoDB, a powerful NoSQL database, for efficient data storage and retrieval.
3. **Efficient JSON Request Handling**  : User registration information is submitted to the platform in the form of JSON requests. The infrastructure efficiently handles these requests by utilizing dedicated threads from a thread pool. This ensures concurrent processing and responsiveness to handle a large volume of requests.
4.  **Singleton Command Factory** : The infrastructure incorporates a Singleton Command Factory that serves as a central repository for storing and executing functions associated with various operations triggered by JSON requests. It provides a flexible architecture, allowing for the addition of new features by dynamically loading additional JAR files. This extensible approach empowers companies to seamlessly integrate new functionalities into the platform.
5.  **Dir Monitor** : Future Enhancement. The Dir Monitor module utilizes the Observer design pattern to actively monitor a designated folder. This folder allows companies to add JAR files containing additional commands as needed. When a new JAR file is added to the monitored folder, it is automatically detected and loaded into the Singleton Command Factory. This enables the execution of newly added commands without any manual configuration, offering a plug-and-play capability.

    
## Flow

![image](https://github.com/RacheliSeliger/SmartDevices/assets/132372245/15a40933-7db7-4091-9a0d-01023b85eb40)

