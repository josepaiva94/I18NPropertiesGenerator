# I18NPropertiesGenerator
Script written in Java to generate i18n properties file for translation of JSF Applications


# How to use it?
  - Input files should be placed under resources/ folder
  - Files with terms should be named terms_{locale}(.txt)
  - The files with keys should be named keys(.txt) (If this file does not exists, the english terms file will be used to generate the keys. If there is also no file with english terms, a "random" terms file is selected)
  - Generated files are placed in resources/output/ folder
