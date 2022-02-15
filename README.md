# DagashiApp-android

Unofficial [Android Dagashi](https://androiddagashi.github.io/) application

## Requirements
- Android Studio Chipmunk


## Architecture

```mermaid
  graph TD;
      :app-->:ui;
      :ui-->:repository;
      :ui-->:model;
      :repository-->:model;
      :repository-->:data;
      :data-->:model;
```
