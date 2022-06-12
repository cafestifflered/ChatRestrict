# ChatRestrict Scheduler
Disable or Enable chat and command access for specific time periods for specific players.

## Rules:
Rules are used to determine the condition in which a player may or may not be able to talk or send a command.
Simply add new rule types to the ``rules.yml`` to add them.

### General Rule Logic:
- If the rule returns true, they are able to talk.
- If the rule returns false, they are unable to talk.
- The opposite occurs for "flip" and "disallowed_command" type rules.

### Main Syntax
Each rule is given a name, type, and the parameters relating to that type.
For example, this creates a rule named ``weekend-check`` which allows chat on Saturday and Sunday.
```yaml
rules:
  weekend-check:
    type: "allowed_weekdays"
    week_days: ["SATURDAY", "SUNDAY"]
```

### Default rules.yml
This is the default rules.yml file which includes a ruleset (with two contained rules) and two other separate rules. With these default values, chat is allowed on every day of the week except between 11:59 PM and 12:00 AM. Three fake commands are also blocked, regardless of if the ruleset is true. The time between 4:32 AM and 4:33 AM has chat blocked, also regardless of whether other rules are true.

For example, if the server admin wanted they could add a "disallowed_commands" rule type to a ruleset, along with rules for specific times and days of the week, to restrict those commands on a specific schedule (unless the player has a bypass permission).
```yaml
rules:
  chat-allowed:
    type: "ruleset"
    rules:
      chat-days:
        type: "allowed_weekdays"
        week_days: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]
      chat-time:
        type: "allowed_timerange"
        time_min: "12:00 AM"
        time_max: "11:59 PM"
        zone: "EST"
  command-blocklist:
    type: "disallowed_commands"
    commands: ["cr_comma", "cr_separated", "cr_list_of_commands"]
  flipped-rule:
    type: "flip"
    rule:
      type: "allowed_timerange"
      time_min: "4:32 AM"
      time_max: "4:33 AM"
      zone: "EST"
```

### Types:
There are five types of rules which admins can create:
- allowed_weekdays
- allowed_timerange
- disallowed_commands
- flip
- ruleset


### allowed_weekdays
Returns true if the current day of the week is in the ``week_days`` list.
#### Syntax
```yaml
rules:
  my-cool-rule:
    type: "allowed_weekdays"
    week_days: ["MONDAY", "TUESDAY"]
```


### allowed_timerange
Returns true if the current time is within the provided timestamps. 

#### Syntax
```yaml
rules:
  my-cool-rule:
    type: "allowed_timerange"
    time_min: "7:00 AM"
    time_max: "8:00 PM"
    zone: "EST"
```
- ``time_min`` and ``time_max`` 
  - Supports 24 hour time.
  - When using time periods (AM/PM) ensure there is a space and they are capital.
- ``zone``
  - Supports long timezones ``America/Los_Angeles`` (recommended)
  - Supports short timezones ``EST``
  - Supports time offsets ``-05:00``
    - When using timezones, time offsets are automatically calculated.


### disallowed_commands
Returns true if the command the player executes does not start with any of the provided ``commands``.
#### Syntax
  ```yaml
rules:
  my-cool-rule:
    type: "disallowed_commands"
    commands: ["say", "tell", "msg"]
```


### flip
Flips the result of the rule provided in the ``rule`` section.
#### Syntax
```yaml
rules:
  my-cool-rule:
    type: "flip"
    rule:
      type: "allowed_weekdays"
      week_days: ["MONDAY", "TUESDAY"]
```
In this case, players are able to talk on days that are NOT Monday or Tuesday.


### ruleset
Goes through the contained set of ``rules`` and will return true if all pass, or false if at least one of them fails.
This is the base structure used in the configuration file.
#### Syntax
```yaml
rules:
  my-cool-rule:
    type: "ruleset"
    rules:
      my-cool-rule:
        type: "allowed_weekdays"
        week_days: ["MONDAY", "TUESDAY"]
      my-cool-rule-2:
        type: "allowed_weekdays"  
        week_days: ["SUNDAY", "MONDAY"]
```


## Permissions:

- Bypass All Rules & Restrictions: `chatrestrict.bypass.all`
- Bypass Rule `chatrestrict.bypass.<rulename>`
- Reload `chatrestrict.reload`
- Enable/Disable Rules `chatrestrict.toggle`

## Commands:

- `/chatrestrict reload` (Reloads the config)
- `/chatrestrict toggle` (Toggles between chat restricted and chat enabled modes)
- `/chatrestrict enable` (Enables all rules and puts the server in chat restricted mode)
  - `/chatrestrict enable <rulename>` (Enables a specific rule to filter chat)
- `/chatrestrict disable` (Disables all rules and puts the server in chat allowed mode)
  - `/chatrestrict disable <rulename>` (Disables a specific rule from filtering chat)
- `/muteall` (Disables chat, regardless of enabled rules)
- `/unmuteall` (Enables chat and follows enabled rules)

## Future Plans:

- Push chat messages through to staff who have a specific permission
- Support for DiscordSRV to push chat messages through
- Support for CarbonChat channels
- Support for party, plot, towny, and faction plugins with private chat channels
- Config option to automatically restrict chat when all staff members are offline
