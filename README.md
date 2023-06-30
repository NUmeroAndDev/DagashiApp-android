# DagashiApp-android

Unofficial [Android Dagashi](https://androiddagashi.github.io/) application

## Requirements
- Android Studio Giraffe


## Architecture

```mermaid
  graph TD;
      :app-->:feature:*;
      :app-->:data-impl;
      :feature:*-->:navigation;
      :feature:*-->:data;
      :feature:*-->:model;
      :feature:*-->:ui;
      :data-impl-->:data;
      :data-impl-->:model;
      :data-->:model;
```

## Features

- Material 3
  - Dynamic color
- Adaptive Design
- Baseline profiles
- [WIP] AppWidget
