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