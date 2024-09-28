# DagashiApp-android

Unofficial [Android Dagashi](https://androiddagashi.github.io/) application

## Requirements
- Android Studio Jellyfish


## Architecture

```mermaid
  graph TD;
      :app-->:feature:*;
      :app-->:data-impl;
      :feature:*-->:data;
      :feature:*-->:ui;
      :data-impl-->:data;
```

## Features

- Material 3
  - Dynamic color
- Adaptive Design
- Baseline profiles
- AppWidget
