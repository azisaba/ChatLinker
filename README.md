## Chat Linker
Discordのチャットを連携しよう!

## Requirements
以下の環境変数が必要です。

- DISCORD_BOT_TOKEN: ディスコードのボットのtoken
- DATABASE_HOST: データベースのホスト
- DATABASE_NAME: データベースの名前
- DATABASE_USER: データベースのユーザー名
- DATABASE_PASS: データベースのパスワード

## Commands
- `/link add <from> <to>`: <from>から<to>への連携を行ないます。(デフォルトでは双方向に連携します)
- `/link remove <from>`: <from>からの連携を解除します。
- `/link get <from>`: <from>からの連携を確認します。
