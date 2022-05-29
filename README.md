# ChatRestrict Scheduler
Disable or Enable chat access for specific time periods.

## Config Options:

- Set the time zone for the server
- Set specific days of the week that chat is enabled for everyone
- Set specific times during each day of the week that chat is enabled for everyone
- Set commands that are blocked during the time period
- Message to send when chat message is blocked (support for MiniMessage color codes)
- Message to send when chat restricted mode is toggled manually or automatically on schedule

## Predicates:
Predicates are used to determine the condition in which a player may/maynot be able to talk or send a command.
Simply add new predicate types to the ``predicates`` section in the ``config.yml`` to add them.

### Predicate Logic:
- If the predicate returns true, they are able to talk.
- If the predicate returns false, they are unable to talk.

This logic can be configured by changing ``predicate-terminate-on``, default is false.

### Types:

### valid_weekdays
Returns true if the current week day is in the ``week_days`` list.
#### Syntax
```yaml
my-cool-predicate:
  type: "valid_weekdays"
  week_days: ["MONDAY", "TUESDAY"]
```

### valid_timerange
Returns true if the current time is within the provided timestamps. 

#### Syntax
```yaml
my-cool-predicate:
  type: "valid_timerange"
  time_min: "7:00 AM"
  time_max: "8:00 PM"
  zone: "EST"
```
#### Notes:
- ``time_min`` and ``time_max`` 
  - Supports 24 hour time.
  - When using time periods (AM/PM) ensure there is a space and they are capital.
- ``zone``
  - Supports long timezones ``America/Los_Angeles`` (recommended)
  - Supports short timezones ``EST``
  - Supports time offsets ``-05:00``
    - When using timezones, time offsets are automatically calculated.

### invalid_commands
Returns false if the command the player executes starts with any of the provided ``commands``.
#### Syntax
  ```yaml
my-cool-predicate:
  type: "invalid_commands"
  commands: ["say", "tell", "msg"]
```

### flip
Flips the result of the predicate provided in the ``predicate`` section.
#### Syntax
```yaml
my-cool-predicate:
type: "flip"
predicate:
    type: "valid_weekdays"
    week_days: ["MONDAY", "TUESDAY"]
```
In this case, players are able to talk on days that are NOT monday and tuesday.

### compound
Goes through the ``predicates`` and will return true if all pass, and false if at least one of them fails.
This is the base structure used in the configuration file.
#### Syntax
```yaml
my-cool-predicate:
type: "compound"
predicates:
  my-cool-predicate:
    type: "valid_weekdays"
    week_days: ["MONDAY", "TUESDAY"]
  my-cool-predicate-2:
    type: "valid_weekdays"  
    week_days: ["SUNDAY", "MONDAY"]
```

## Permissions:

- Chat Restricted Mode Bypass: `chatrestrict.bypass`
- ChatRestrict Reload `chatrestrict.reload`
- ChatRestrict Toggle `chatrestrict.toggle`

## Commands:

- `/chatrestrict reload` (Reloads the config)
- `/chatrestrict toggle` (Toggles between chat restricted and chat enabled modes)
- `/chatrestrict enable` (Puts server in chat restricted mode)
- `/muteall` (Alias for `/chatrestrict enabled`)
- `/chatrestrict disable` (Puts server in chat allowed mode)
- `/unmuteall` (Alias for `/chatrestrict disabled`)

## Future Plans:

- Create different groups that follow different schedules
- Push chat messages through to staff who have a specific permission
- Support for DiscordSRV to push chat messages through
- Support for CarbonChat channels
- Support for party, plot, towny, and faction plugins with private chat channels
- Config option to automatically restrict chat when all staff members are offline
