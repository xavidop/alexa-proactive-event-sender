[![circleci](https://circleci.com/gh/xavidop/alexa-proactive-event-sender.svg?style=shield )](https://app.circleci.com/pipelines/github/xavidop/alexa-proactive-event-sender)
[![codecov](https://codecov.io/gh/xavidop/alexa-proactive-event-sender/branch/master/graph/badge.svg)](https://codecov.io/gh/xavidop/alexa-proactive-event-sender)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9f9d71d7893a4fac9f4a5888d3b5b1a0)](https://www.codacy.com/manual/xavierportillaedo/alexa-proactive-event-sender?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xavidop/alexa-proactive-event-sender&amp;utm_campaign=Badge_Grade)

## Proactive notification java client sender

This project aims at simplifying sending proactive events to a skill from an external java process. 

This code performs the following operations:
1. Obtains an authentication token from Alexa by using the skill's ```client_id``` and ```client_secret```. You can obtain these in the Build Tab - Permissions section of your skill in the Alexa developer console (if they are not visible try enabling a permission such as reminder and then disable it).
2. Uses the authentication token obtained in the previous step to *multicast* notifications to those users of the skill that granted permissions to notifications in the Alexa app. Sends events following the [```AMAZON.MessageAlert.Activated```](https://developer.amazon.com/docs/smapi/schemas-for-proactive-events.html#message-alert) schema.

### Adding proactive events to your skill

Remember that if you want to send proactive events to your skill, you need to modify the skilll manifest (skill.json) to add notification permissions to the skill and declare the schema(s) of the proactive events your skill is allowed to send. Using SMAPI, you need to:

1. Add the ```permissions``` object to the skill's ```manifest``` property (skill.json):

<p>

    "permissions": [
        {
            "name": "alexa::devices:all:notifications:write"
        }
    ]

2. Add the ```events``` object to the skill's ```manifest``` property:

<p>

    "events": {
      "publications": [
        {
          "eventName": "AMAZON.MessageAlert.Activated"
        }
      ],
      "endpoint": {
        "uri": "** TODO: REPLACE WITH YOUR Lambda ARN after created **"
      },
      "subscriptions": [
        {
          "eventName": "SKILL_PROACTIVE_SUBSCRIPTION_CHANGED"
        }
      ]
    }

3. Redeploy your skill by running:

<p>

    ask deploy -t skill

If you want to declare that the skill accepts more than one schema, just add them into ```events.publications``` above and remember to change the template in your ```Event``` class from package ```com.xavidop.alexa.model.event```.

The process is more thoroughly described in the official [Alexa Proactive Events API documentation](https://developer.amazon.com/docs/smapi/proactive-events-api.html#onboard-smapi). 

You can find a working example on how to configure your skill for Proactive Events in the official Alexa Github repository: https://github.com/alexa/alexa-cookbook/tree/master/feature-demos/skill-demo-proactive-events

### Prerequisites

You need Java >= 1.8 to run the code and Maven to download the required dependencies.

How to install these two tools goes beyond the scope of this document.

### Install

To download project dependencies simply run the following in the project root:

```xml
    <dependency>
      <groupId>com.xavidop.alexa</groupId>
      <artifactId>alexa-proactive-event-sender</artifactId>
      <version>LATEST</version>
    </dependency>
```

### How use it

After added the dependecy you can use the client as below:

```java
    String clientId = "YOUR-CLIENT";
    String secretId = "YOUR-SECRET";

    AlexaProactiveEventSenderClient client = new AlexaProactiveEventSenderClient(clientId, secretId);

    ProactiveEvent event = new ProactiveEvent();
    event.getEvent().getPayload().getMessageGroup().getCreator().setName("Test");

    URLRegion urlRegion = new URLRegion();
    urlRegion.setRegion(Region.NA);
    urlRegion.setEnvironment(Environment.DEV);

    client.sendProactiveEvent(event, urlRegion);
```
    
* ```Environment```: whether the target events will be sent to the ```live``` or ```development``` endpoints. Allowed values are ```dev``` and ```pro```.
* ```Region```: identifies the region of the Alexa endpoint to use to send proactive events. Allowed values are ```EU``` (Europe), ```NA``` (North America) and ```FE``` (Far East). **Remember**: if your users are located in NA and you are sending events trough the EU endpoint, users located in NA won't receive any notification. If you're using an Alexa Hosted Skill set the endpoint to NA.
* ```message```: this is optional and lets you override the message property defined in [```skills.json```](./skills.json). If you pass the message through this argument, the same message will be sent to all skills configured in [```skills.json```](./skills.json).

These are the values by default of an event when you create it:

```json
    {
        "timestamp": "",
        "referenceId": "UUID-AUTOGENERATED",
        "expiryTime": "",
        "event": {
          "name": "AMAZON.MessageAlert.Activated",
          "payload": {
            "state": {
              "status": "UNREAD",
              "freshness": "NEW"
            },
            "messageGroup": {
              "urgency": "URGENT",
              "creator": {
                "name": ""
              },
              "count": 1
            }
          }
        },
        "relevantAudience": {
            "type": "Multicast",
            "payload": {}
        }
    }
```

That's all!

Happy coding!
