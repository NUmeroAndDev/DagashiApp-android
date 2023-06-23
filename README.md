# DagashiApp-android

Unofficial [Android Dagashi](https://androiddagashi.github.io/) application

## Requirements
- Android Studio Flamingo


## Architecture

```mermaid
  graph TD;
      :app-->:feature:*;
      :feature:*-->:navigation;
      :feature:*-->:repository;
      :feature:*-->:model;
      :feature:*-->:ui;
      :repository-->:model;
      :repository-->:data;
      :data-->:model;
```

## Features

- Material 3
  - Dynamic color
- Adaptive Design
- Baseline profiles
- [WIP] AppWidget
