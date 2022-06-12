# ChatRestrict Scheduler
Disable or Enable chat access for specific time periods.

## Rules:
Rules are used to determine the condition in which a player may/maynot be able to talk or send a command.
Simply add new rule types to the ``rules`` section in the ``config.yml`` to add them.

### General Rule Logic:
- If the rule returns true, they are able to talk.
- If the rule returns false, they are unable to talk.

### Main Syntax
Each rule is given a name and if it is enabled or not.
For example, this creates a rule named ``weekend_check`` which is currently ``disabled``.
```yaml
weekend-check:
  type: "allowed_weekdays"
  week_days: ["MONDAY", "TUESDAY"]
```

### Types:

### allowed_weekdays
Returns true if the current week day is in the ``week_days`` list.
#### Syntax
```yaml
my-cool-rule:
  type: "allowed_weekdays"
  week_days: ["MONDAY", "TUESDAY"]
```

### allowed_timerange
Returns true if the current time is within the provided timestamps. 

#### Syntax
```yaml
my-cool-rule:
  type: "allowed_timerange"
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

### disallowed_commands
Returns true if the command the player executes does not start with any of the provided ``commands``.
#### Syntax
  ```yaml
my-cool-rule:
  type: "disallowed_commands"
  commands: ["say", "tell", "msg"]
```

### flip
Flips the result of the rule provided in the ``rule`` section.
#### Syntax
```yaml
my-cool-rule:
type: "flip"
rule:
    type: "allowed_weekdays"
    week_days: ["MONDAY", "TUESDAY"]
```
In this case, players are able to talk on days that are NOT monday and tuesday.

### ruleset
Goes through the ``rules`` and will return true if all pass, and false if at least one of them fails.
This is the base structure used in the configuration file.
#### Syntax
```yaml
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

- ChatRestrict Bypass All Rules & Restrictions: `chatrestrict.bypass.all`
- ChatRestrict Reload `chatrestrict.reload`
- ChatRestrict Enable/Disable Rules `chatrestrict.toggle`
- ChatRestrict Bypass Rule `chatrestrict.bypass.rulename`

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
