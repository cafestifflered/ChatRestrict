# ChatRestrict Scheduler
Disable or Enable chat access for specific time periods.

## Config Options:

- Set the time zone for the server
- Set specific days of the week that chat is enabled for everyone
- Set specific times during each day of the week that chat is enabled for everyone
- Set commands that are blocked during the time period
- Message to send when chat message is blocked (support for MiniMessage color codes)
- Message to send when chat restricted mode is toggled manually or automatically on schedule

## Permissions:

- Chat Restricted Mode Bypass: `chatrestrict.bypass`
- ChatRestrict Reload `chatrestrict.reload`
- ChatRestrict Toggle `chatrestrict.toggle`

## Commands:

- `/chatrestrict reload` (Reloads the config)
- `/chatrestrict toggle` (Toggles between chat restricted and chat enabled modes)
- `/chatrestrict enable` (Puts server in chat restricted mode)
- `/muteall` (Alias for `/chatrestrict enabled`)
- `/chatrestrict disabled` (Puts server in chat allowed mode)
- `/unmuteall` (Alias for `/chatrestrict disabled`)
