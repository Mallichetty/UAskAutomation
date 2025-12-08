## UAsk Chatbot Automation Framework
**UI + Semantic Validation** using **Gemini Embeddings** (Java, Selenium, Cucumber, Allure)

This automation framework validates both UI workflows and AI chatbot responses semantically using:

- **Selenium WebDriver (UI automation)**
- **Cucumber BDD (Gherkin features)**
- **Gemini Embeddings API (semantic validation for Gen-AI responses)**
- **Java + JUnit**
- **Allure Reporting**

This framework ensures the chatbot responses are:

- **Contextually correct**
- **Semantically aligned with expected output**
- **Not hallucinating**
- **Accurate and robust**

Also the framework emphasizes:

- **Cross-platform testing:** Web and Mobile Responsiveness
- **API automation:** REST API validation
- **AI/ML validation:** Semantic response validation using embeddings
- **Reusable and maintainable code** with a modular architecture

## Setup Instructions
- **Java 17 and above**
- **Selenium WebDriver**
- **Cucumber BDD**
- **Page Object Model (POM)**
- **Maven** (Build + Dependency Management)
- **JUnit/TestNG** (Runner & Assertions)

## 🚀 Features
**✅ Selenium UI Automation**
- **Opens chatbot UI**
- **Logs in (page object model)**
- **Types and submits a question**
- **Waits until “Working on it” disappears**
- **Extracts chatbot answer**
  
**✅ AI Semantic Validation (Gemini Embeddings)**

We use Gemini Embedding Model: **models/gemini-embedding-001**

The validator:

- **Fetches embedding of expected text**
- **Fetches embedding of actual chatbot output**
- **Computes cosine similarity**
- **Passes when similarity ≥ 0.75**


  
## Prerequisites/Setup Instructions

1. **Java 17** installed → verify with:
   ```bash
   java -version

2. **Maven** installed → verify with:
   ```bash
   mvn -v
   
3. Chrome Browser installed.
4. ChromeDriver handled by WebDriverManager (auto-managed, no manual setup required).

## Utilities
1. WebDriverUtils.java → Wrapper methods (click, type, waits, moveToElement, etc.)
2. log4j2.xml → Centralized logging
3. Config.properties → Environment and test configuration
4. Hooks.java → Cucumber hooks (setup & teardown)

▶️ Running Tests

1. **Run all scenarios** and **Run tests with a specific tag**
   ```bash
   mvn clean test
   mvn clean test -Dcucumber.filter.tags="@PositiveCases"

2. **Generate Allure Report**
   ```bash
   mvn allure:serve
