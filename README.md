# 🦊 Youkai

Youkai is a [Minecraft Resourcepack](https://minecraft.wiki/w/Resource_pack) compiler/generator

It can do following:
- Generate item models with custom textures automatically just from texture file
- Compile all custom models into one item with automatically assigned custom model data
- Send data to server-side plugin with all of compiled information so the server can parse it
- Obfuscate resourcepack files
- Obfuscate shader files
- Break headers of the final zip file so programs like winrar/7zip cant extract it 
- Automatically upload compiled resourcepack file to GCP Bucket/Amazon S3/Cloudflare R2/MS Azure Blob Storage/FTP Servers

Youkai also provides extensive web api and in the future also front-end gui for easily managing your resourcepacks

> [!WARNING]  
> Youkai is work in progress and most features are not implemented yet. Do not use in production.

_Examples and documentation more will be added here in the future_