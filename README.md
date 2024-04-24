# TTranslator ![Java](https://img.shields.io/badge/language-Java-red.svg) ![Gradle](https://img.shields.io/badge/build-Gradle-blueviolet.svg) ![Google API](https://img.shields.io/badge/Google%20API-v2.0.0-green)

TTranslator is a powerful Minecraft translation tool. It enhances server accessibility by allowing UI and language
configuration on both client-side and server-side.

## Features

- Configurable in-game language
- Configurable server key translation
- Toggleable translated languages
- Player UI configuration

## Installation

1. Clone the repository.
2. Run `./gradlew build`.
3. Copy the generated TTranslator.jar file into your Minecraft's mod directory.

## Configuration

In the mod settings in Minecraft interface, select TTranslator. Here you can configure Google key and translated
languages via the in-game UI.

## Contribution and Improvements

Any contributions towards the below features are welcome:

1. **Real-time translation** - Implementation of real-time translation for text-based items.
    - Required knowledge: Java, Google Translate API
2. **UI Overhaul** - Enhance user interface for better user experience.
    - Required knowledge: Java, Minecraft Modding
3. **Translation key security** - Improve the security for the translation key.
    - Required knowledge: Java
4. **Translation APIs on-demand changes** - Add support for other translation APIs.
    - Required knowledge: Java, Minecraft Modding

Please create a fork, implement the feature, and raise a pull request against the main branch if you are interested in
contributing.

## License

The project is licensed under the [MIT License](http://opensource.org/licenses/MIT).