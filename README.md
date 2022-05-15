# DagashiApp-android

Unofficial [Android Dagashi](https://androiddagashi.github.io/) application

## Requirements
- Android Studio Chipmunk


## Architecture

```mermaid
  graph TD;
      :app-->:ui;
      :ui-->:navigation;
      :ui-->:repository;
      :ui-->:model;
      :repository-->:model;
      :repository-->:data;
      :data-->:model;
```

## Features

- Material 3
  - Dynamic color
- Adaptive Design
- [WIP] AppWidget
