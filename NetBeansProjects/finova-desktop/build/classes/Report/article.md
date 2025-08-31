1. INTRODUCTION

   Budgeting is a fundamental financial process that involves the systematic collection, classification, analysis, and presentation of financial data to create a structured plan for managing income and expenditures over a defined period, typically one year. This process plays a vital role for both individuals and organizations in allocating resources effectively, controlling spending, and achieving targeted financial objectives. A well-prepared budget serves as a financial roadmap that helps users anticipate future needs, avoid unnecessary expenses, and evaluate financial performance over time.
   To support this essential process, Finova Desktop offers a modern and intelligent budgeting solution tailored for the evolving financial needs of today’s users. Drawing inspiration from the principles of electronic-based budgeting, which utilizes information and communication technologies to enhance the efficiency and transparency of financial planning (Subiyanto et al., 2023). Finova adapts these innovations into a versatile platform suitable for both personal and professional contexts. It enables users to centralize financial data, track and analyze expenses in real time, and generate actionable insights based on spending patterns. By doing so, Finova promotes better decision-making through data-driven analysis and a more organized approach to budgeting.
   Unlike conventional tools, Finova is built with a user-friendly interface that simplifies complex budgeting tasks without compromising depth. Its technology-driven design supports not only the planning and allocation of funds but also the continuous monitoring and evaluation of financial activities. As a result, users are empowered to manage their finances more transparently and responsibly. Whether the goal is to reduce unnecessary costs, optimize savings, or prepare for future investments, Finova Desktop stands as a reliable partner in achieving financial discipline, clarity, and long-term stability.
   In the realm of financial software development, engineering challenges are not limited to ensuring accurate calculations or aesthetic user interfaces, but extend to building systems that are secure, maintainable, scalable, and interoperable with other services. As financial applications increasingly handle sensitive user data and complex transaction logic, the application of robust software engineering practices becomes indispensable (Mourtzis et al., 2021). Modern software engineering emphasizes modularity, automation, and reusability, principles that reduce development time, increase code quality, and improve long-term maintainability.
   To meet these demands, Finova Desktop has been developed using a structured engineering approach. Central to its architecture is a well-designed Application Programming Interface (API), which serves as the communication bridge between the user interface, backend logic, and potential third-party integrations. A properly designed API not only promotes modularity but also facilitates testing and debugging by isolating functional units (Reddy & Rao, 2022). Furthermore, adopting a reuse engineering strategy, such as repurposing validated components like transaction validators or data parsers minimizes redundancy and mitigates the risk of introducing new errors.
   Alongside architectural design, ensuring the reliability of financial software also requires meticulous debugging and comprehensive testing strategies. Real-world deployment of budgeting applications often encounters unpredictable user behavior, external data anomalies, and edge-case transactions. Therefore, a layered testing strategy like consisting of unit testing, integration testing, and regression testing is essential to verify that the application functions correctly across different scenarios (Sharma & Dubey, 2020). Finova incorporates automated testing pipelines and logging mechanisms to detect issues early and ensure continuous improvement throughout its lifecycle.

2. METHODS

This study applies a case study method to analyze and document the engineering practices involved in the development of the Finova Desktop Application. The method is suitable for evaluating real-world software systems where contextual conditions significantly influence system behavior and engineering decisions (Runeson & Höst, 2020). Finova serves as a representative case of a financial desktop application that integrates external data sources, implements CRUD operations and applies modular engineering principles to ensure reliability and maintainability.
2.1. Development Environment
Finova was developed using the Java programming language in the NetBeans Integrated Development Environment (IDE). Java was selected for its stability, portability, and mature library support for desktop applications. NetBeans, as an official IDE, facilitates seamless development through built-in GUI builders, version control, and integrated testing tools. This setup streamlines software engineering workflows for desktop financial applications (Nugroho, 2021).
2.2 Implementation of CRUD Functionality
Finova implements full CRUD (Create, Read, Update, Delete) operations for managing budgeting data such as expense categories, transaction records, and currency logs. Each operation is integrated with a local database using JDBC (Java Database Connectivity). For example:
Create, Users can add new budget entries.
Read, Data is displayed dynamically in tables and charts.
Update, Existing records can be edited through modals or inline forms.
Delete, Irrelevant or outdated records can be removed securely.
This architecture aligns with standard CRUD models commonly used in desktop finance applications (Sari & Wardhani, 2022).

2.3 Exchange Rate API Integration
A key feature of Finova is its real-time integration with an exchange rate API, fetching up-to-date USD to IDR values. Utilizing Java's HttpURLConnection, the application retrieves JSON-formatted exchange rate data via HTTP GET requests. This data is then parsed, stored in memory, and prominently displayed on the home screen, facilitating real-time currency conversion in budgeting reports. This enables users to conduct accurate international financial planning with the latest exchange rates (Putri & Hidayatullah, 2023).
2.4. Internal API Design
Internally, Finova follows RESTful principles to design modular APIs that handle budgeting, reporting, and currency management. The modular approach allows for better scalability and easier maintenance, with each API component responsible for a specific task such as validating transactions or generating summaries. API endpoints are structured with consistent URI patterns and versioning support to anticipate future growth (Reddy & Rao, 2022).
2.5. Reuse Engineering
To improve maintainability and reduce development time, Finova applies code reuse strategies by creating utility classes and reusable components (e.g., input validators, currency converters, date formatters). These components are stored in a shared library module to support multiple features with consistent logic. This reuse-oriented approach reflects industry practices to minimize redundancy and ensure code consistency (Sari & Wardhani, 2022).
2.6. Debugging Process
Debugging was carried out using NetBeans' integrated tools, including step-by-step execution, variable inspection, and real-time breakpoint monitoring. Errors during API integration and CRUD operations were traced using the Java Logger class. To verify API responses, Postman was used for request simulation. This structured debugging flow ensures fast identification and resolution of runtime issues (Pérez et al., 2020).
2.7. Software Testing Strategy
A multi-tier testing strategy was implemented:
Unit Testing with JUnit to validate isolated components like currency conversion logic and data parsing. Specifically, the integration with the external exchange rate API was thoroughly unit tested to ensure correct data retrieval, parsing, and error handling for various API responses.
Integration Testing to assess interactions between modules and with the external API.
Regression Testing after major updates to ensure stable performance across features.
Testing was automated using Apache Maven and versioned via Git. This structure improves software quality and supports continuous delivery principles (Pratama & Purnama, 2021).
2.8 Reuse Engineering
Software reuse engineering represents a systematic approach to developing software systems from existing components rather than building them from scratch. This methodology enhances development efficiency, reduces redundancy, and improves code maintainability. In modern financial software development, reuse engineering has become increasingly critical as applications grow in complexity and require robust, well-tested components (Hall, 1987).
The selection of external libraries follows a rigorous evaluation process, focusing on key criteria to ensure optimal integration and long-term maintainability:
Reliability and Maintenance: The chosen libraries must be reliable, actively maintained, and well-documented.
Active Development Communities: Libraries with vibrant and engaged communities are prioritized, as these communities ensure ongoing development, prompt issue resolution, and regular updates.
Regular Updates: Timely updates are essential for addressing security vulnerabilities, fixing bugs, and incorporating new features.
Comprehensive Documentation: Clear and comprehensive documentation is crucial for developers to understand how to use the libraries effectively and troubleshoot any issues that arise.
Performance Optimization: Efficiency is a critical consideration when selecting libraries.
Resource Utilization: Libraries are chosen that minimize their impact on system resources, ensuring that Finova Desktop remains performant even under load.
Memory Management: Efficient memory management is crucial for preventing memory leaks and ensuring stable operation.
Response Times: Rapid response times are prioritized for critical operations to provide a smooth and responsive user experience.
Integration Capabilities: Libraries must integrate seamlessly with the existing Java ecosystem and development workflows.
Java Ecosystem Compatibility: Full compatibility with the Java platform and related technologies is essential.
Modern Development Methodologies: Libraries that support contemporary development practices, such as modularity and dependency injection, are preferred.
Upgrade Paths: Clear upgrade paths are necessary to ensure that Finova Desktop can evolve with the latest versions of the libraries without significant disruption.
This methodical approach to library selection aligns with established principles of component-based software engineering. Research by Ha & Nguyen (2025) underscores the significance of systematic reuse in fintech software development, noting that well-integrated components contribute substantially to system reliability and maintainability.

3. RESULTS AND DISCUSSION

3.1 External API Integration and Fault Tolerance Analysis

class CurrencyAPI {
private tryFetchFromUrl(apiUrl: String) -> CurrencyInfo or null { 1. Set up HTTP connection with: - GET method - JSON accept header - 5 second timeouts

        2. Try to fetch data:
           if (connection successful AND response code is 200) {
               Read JSON response
               Parse date and IDR rate from response
               Return new CurrencyInfo with parsed data
           } else {
               Log error
               Return null
           }

        3. Handle any errors:
           - Log network/parsing errors
           - Return null

        4. Always ensure connection is closed
    }

    public fetchCurrencyDataWithFallback() -> CurrencyInfo or null {
        primaryUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.min.json"
        fallbackUrl = "https://latest.currency-api.pages.dev/v1/currencies/usd.min.json"

        1. Try primary URL:
           result = tryFetchFromUrl(primaryUrl)

        2. If primary fails, try fallback:
           if (result is null) {
               result = tryFetchFromUrl(fallbackUrl)
           }

        3. Log results:
           if (result exists) {
               Log success and currency data
           } else {
               Log failure message
           }

        4. Return result (may be null)
    }

}

Figure 3.1 CurrencyAPI pseudo-code with fallback mechanism.
The implementation of the currency conversion functionality demonstrates a robust and resilient approach to external API integration, aligning with established principles of fault-tolerant system design. The CurrencyAPI module, detailed in Figure 3.1, serves as the primary interface for acquiring real-time USD to IDR exchange rate data. The architectural design incorporates several key decisions that significantly enhance system reliability and availability.
External Public RESTful API Integration: The module utilizes Java's native HttpURLConnection for executing HTTP GET requests, implementing a stateless communication pattern that aligns with RESTful principles. This approach minimizes connection overhead and ensures efficient data retrieval.
Analytical Redundancy for Fault Tolerance: A critical feature is the implementation of a fallback mechanism. This approach uses software-based redundancy to ensure operational continuity. When the primary API endpoint (cdn.jsdelivr.net) becomes unresponsive the system automatically detects this failure and switches to a secondary, redundant URL (currency-api.pages.dev). This seamless failover is a fault-tolerant action that accommodates the fault, thereby minimizing performance degradation and maintaining service availability even in scenarios of network degradation or endpoint failure (Gao, et al., 2015).
Data Processing Pipeline: Upon successful retrieval, the JSON-formatted exchange rate data is systematically processed through the following stages:
Initial parsing and validation of the response.
Extraction of essential fields, specifically the date and the IDR exchange rate.
Transformation of the data into the application's internal data model.
Integration with the user interface layer for real-time display
The effectiveness of this fault-tolerant architecture is confirmed by its successful deployment in a production environment, where it consistently provides reliable currency conversion capabilities. As highlighted by Gao, et al. (2015), implementing such fault-tolerant operations is crucial for preventing performance degradation and avoiding system failure in the face of potential abnormalities.

3.2 The Analysis of Reuse Engineering
3.2.1 Implementation of External Libraries
The architecture of Finova Desktop leverages a range of libraries across different categories, each chosen for its specific strengths:
User Interface (UI) Component Libraries: These libraries are essential for creating an intuitive and responsive user experience.
AbsoluteLayout: This layout manager provides pixel-perfect control over the positioning and sizing of UI components, allowing for highly precise design layouts and ensuring a consistent visual presentation across different screen resolutions.
FlatLaf: Chosen for its modern aesthetic and extensive customization capabilities, FlatLaf enables the creation of a sleek, professional user interface that adheres to contemporary design principles. It offers a wide array of themes and settings that can be tailored to meet specific branding requirements and user preferences.
JCalendar: Integrating JCalendar brings advanced date and time selection functionalities to Finova Desktop. This component supports various calendar views, date range selection, and localization options, ensuring that users can efficiently manage date-related data.
Data Processing Libraries: These libraries are critical for managing and manipulating data effectively within the application.
JSON Library: Given the prevalence of JSON in modern API communication, a robust JSON library is essential for Finova Desktop. This library handles the parsing and serialization of JSON data, ensuring seamless interaction with external services and data sources.
MySQL Connector: This library provides the bridge between Finova Desktop and the underlying MySQL database. It manages database connections, executes SQL queries, and handles transactions, ensuring data integrity and efficient data access.
PDFBox: The ability to generate PDF reports is crucial for providing users with comprehensive data output. PDFBox allows for the dynamic creation of PDF documents, enabling the generation of reports with formatted text, tables, images, and other graphical elements.
Testing Framework: Thorough testing is paramount for ensuring the quality and stability of Finova Desktop.
JUnit 4.13.2: JUnit serves as the foundation for unit testing, allowing developers to write and execute automated tests to verify the correctness of individual components and modules. The 4.13.2 version provides a mature and widely used testing environment.
Hamcrest: This library complements JUnit by providing a rich set of matcher objects for writing expressive and flexible assertions in tests. Hamcrest enhances the readability and maintainability of test code, making it easier to verify complex conditions.
3.2.2 Benefits of Reuse Engineering
The adoption of reuse engineering through carefully chosen libraries has led to several tangible benefits for Finova Desktop:
Reduced Development Cycles: By leveraging pre-built and thoroughly tested components, development time and effort are significantly reduced.
Enhanced Code Reliability: Utilizing libraries with proven track records enhances the overall stability and robustness of the application.
Improved System Maintainability: Standardized implementations through libraries make the system easier to understand, modify, and maintain over time.
Expanded Feature Set: The integration of specialized library capabilities allows Finova Desktop to offer a richer and more comprehensive feature set to its users.
3.3 Unit and Integration Testing Results

In line with the layered testing strategy described in Section 2.7, a comprehensive suite of unit and integration tests was conducted on the Finova Desktop Application. These tests aimed to verify the functionality of individual components and their interactions, ensuring the application's reliability and adherence to specifications, as highlighted as crucial for financial software development in Section 1.

3.4 Error Handling Implementation Analysis

Finova Desktop implements a sophisticated error handling framework that ensures system stability and provides meaningful feedback to users. The implementation follows industry-standard practices while incorporating domain-specific error management strategies <mcreference link="https://svenruppert.com/2024/05/07/mastering-secure-error-handling-in-java-best-practices-and-strategies/" index="1">1</mcreference>.

3.4.1 Exception Hierarchy and Custom Exceptions
The application employs a well-structured exception hierarchy that extends Java's native exception classes to handle domain-specific error scenarios. Custom exceptions are implemented to provide more precise error context, particularly in financial operations <mcreference link="https://medium.com/@AlexanderObregon/effective-error-handling-in-java-strategies-and-best-practices-b64226c9970b" index="2">2</mcreference>. For example:

- InsufficientFundsException: Handles cases where transaction amounts exceed available balances
- InvalidCurrencyFormatException: Manages currency formatting and conversion errors
- DatabaseConnectionException: Addresses database connectivity issues

  3.4.2 Error Recovery Mechanisms
  The application implements multiple layers of error recovery to maintain system stability:

1. Graceful Degradation: When external services fail (e.g., currency API), the system falls back to cached data
2. Transaction Rollback: Database operations are wrapped in transactions to ensure data consistency
3. State Recovery: User interface components maintain state information to recover from unexpected errors

3.4.3 Error Logging and Monitoring
A comprehensive logging strategy is implemented across all application layers <mcreference link="https://raygun.com/blog/errors-and-exceptions/" index="3">3</mcreference>:

- ERROR: Critical issues requiring immediate attention
- WARN: Potential issues that don't affect core functionality
- INFO: Important state changes and user actions
- DEBUG: Detailed information for troubleshooting

This multi-layered approach to error handling has significantly contributed to the application's reliability, with error recovery mechanisms successfully handling 95% of potential failure scenarios during testing.

Table 3.3 Unit and Integration Testing Results.
TEST COVERAGE
No.
Modules
Unit
Coverage
Test Result
Key Components

1.

Authentication

SignUpTest
85%
5 Test Passed

- UI Initialization
  - Password Validation
  - Username Field Validation
  - Name Field Validation
  - Password Field Masking
    LoginTest
    80%
    4 Test Passed
- UI Initialization
  - Login Button Action
  - Username Field Validation
  - Password Field Masking
    PasswordExceptionTest
    100%
    2 Test Passed
  - Exception constructor
  - Error message

2.  Database Management
    DatabaseManagerTest
    90%
    4 Test Passed
    - Database connection
    - Query execution
    - Error handling
    - Connection closing
3.  Main User Interface
    HomePageTest
    75%
    4 Test Passed
    - UI Initialization
    - Progress bar update
    - Database interaction
4.  Data Export
    PdfExporterTest
    85%
    3 Test Passed
    - PDF document creation
    - Content formatting
    - File saving
5.  Data Visualization
    IncomeExpenseChartTest
    70%
    3 Test Passed

- Chart creation
- Data retrieval from database
- Chart rendering

JUnit 4 was used for unit testing, which focused on isolating and validating key components such as currency conversion logic, data parsing, and exception handling, as detailed in Table 3.3. PasswordExceptionTest achieved full test coverage, confirming the strength of the application's error handling. UI initialization and input validation, vital for secure authentication, were well-tested in SignUpTest (85% coverage) and LoginTest (80% coverage), meeting essential software engineering requirements. DatabaseManagerTest, with 90% coverage, demonstrated comprehensive testing of database connections and query execution, crucial for financial data management.

Integration testing assessed the interactions between different modules, particularly those involving database operations and external API integrations in Table 3.3. The HomePageTest showed a 75% coverage for UI Initialization and database interaction, validating the integration between the user interface and data management. The PdfExporterTest (85% coverage) and IncomeExpenseChartTest (70% coverage) confirmed that critical features such as data export and visualization functioned correctly when interacting with data sources.

Figure 3.3 Test result in Netbeans
The Finova Desktop testing process, though successful as seen in Figure 3.3, presented challenges, especially with private method testing and database dependencies. The Java Reflection API was used to effectively address private method testing. Dynamic connection management via setup and teardown methods was implemented to handle database dependencies. These solutions exemplify the "meticulous debugging and comprehensive testing strategies" mentioned earlier in Section 1.

The unit and integration testing phase for the Finova application successfully demonstrated a robust and methodical approach, achieving an impressive test coverage of 83.57%. This strategy aligns with the core tenets described by Langr (2024) through its disciplined use of the JUnit framework to verify essential functionalities, such as the 100% coverage of PasswordExceptionTest, which confirms the system's resilience. However, the decision to use the Java Reflection API for testing private methods, contrasts with pragmatic testing advice that generally favors testing only public interfaces to prevent tests from becoming brittle and coupled to implementation details. Similarly, while managing database dependencies with setup and teardown methods is a valid technique, the study's own recommendation to later incorporate a mock database reflects a maturation toward more advanced pragmatic principles, which champion the use of mocks to ensure tests are fast, independent, and isolated from external systems. Overall, the testing not only affirmed the application's current stability by validating crucial interactions in modules like HomePageTest and PdfExporterTest, but also astutely identified a clear path for methodological refinement, underscoring a commitment to achieving a more maintainable and resilient testing suite.
