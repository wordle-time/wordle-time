package com.wordletime.extensions

import java.io.File

fun MutableMap<String, String>.addIfFileExists(resourcePath: File, key: String, fileName: String) {
  if(resourcePath.resolve(fileName).exists()) put(key, fileName)
}
