Feature: U-Ask Chatbot AI Response Validation
  As a user
  I want to validate the AI responses of the U-Ask chatbot
  So that they are accurate, relevant, and not hallucinated

  @UAskChatBotFeature
  Scenario Outline: Validate AI response for given user input : "<userMessage>"
    Given I login with valid credentials
    When I send the user input "<userMessage>"
    Then the chatbot response should match the expected result from JSON

    @PositiveCases
    Examples:
      | userMessage                     |
      | How do I renew my Dubai visa?   |
      | What is the UAE public holiday? |
      | كيف أجدد تأشيرة دبي؟            |

    @NegativeCases
    Examples:
      | userMessage    |
      | Tell me a joke |
      | قل لي نكتة     |

  @UAskChatBotFeature
  Scenario Outline: Verify input field behavior after sending a message and validate chatbot response formatting
    Given I login with valid credentials
    When I send the user input "<userMessage>"
    Then the chatbot should display the sent message "<userMessage>"
    And the input field should be cleared after sending the message
    Then the chatbot response should have clean formatting

    Examples:
      | userMessage    |
      | Tell me a joke |
      | قل لي نكتة     |