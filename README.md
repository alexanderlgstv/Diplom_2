# **Api tests for StellarBurgers project.**

### Annotation.

Project includes api tests for [StellarBurgers](https://stellarburgers.nomoreparties.site/). Tests are completed as a part of training course "QA Automation Engineer (Java)" from Yandex Praktikum.

### Libraries and Frameworks.

Tests are uses:
* Java 11.0.2,
* JUnit 4.13.2,
* Mockito 4.7.0,
* Allure 2.22.0
* Maven 3.8.1.
* Gson 2.10.1
* Maven-surefire-plugin 2.22.2
* RestAssured 5.3.0

### Test coverage

#### **User create:**
* Unique user create;
* Exists user create;
* Create user with null field.

#### User login:
* Exists user login;
* Invalid data user login.

#### User update:
* With authorization;
* Without authorization.

#### Order create:

* With authorization;
* Without authorization;
* With ingredients;
* Without ingredients;
* With invalid hash.

#### Get order list:

* User is logged in;
* User is not logged in.

### Running tests.

Tests run: `mvn clean test`

Allure report run:  `mvn allure:serve`

Allure test coverage: target/allure-results
