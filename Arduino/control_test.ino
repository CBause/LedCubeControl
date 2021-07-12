#include <avr/pgmspace.h>
#include <ArduinoJson.h>



struct Led {
  int x;
  int y;
  int sinkLayer;
  int outputNumber;
};

struct Sink {
  int outputNumber;
};

struct GridPoint {
  int x;
  int y;
  int z;
};



////--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- STRUCTS END ////

GridPoint getGridPoint(int x, int y, int z) {
  GridPoint result;
  result.x = x;
  result.y = y;
  result.z = z;
  return result;
}

////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ HELPER FUNCTIONS END ////

Led LEDARRAY[6][6][6];
Sink SINKARRAY[6];

////--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- GLOBALS END ////

void setupLedStructs() {
  int currentOutput = 2;
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      int currentLayer = 48;
      for (int k = 0; k < 6; k++) {
        LEDARRAY[i][j][k].outputNumber = currentOutput;
        LEDARRAY[i][j][k].sinkLayer = k;
        LEDARRAY[i][j][k].x = i;
        LEDARRAY[i][j][k].y = j;
        pinMode(LEDARRAY[i][j][k].outputNumber, OUTPUT);
        currentLayer++;
      }
      currentOutput++;
    }
  }
}

void setupSinks() {
  for (int i = 48; i < 54; i++) {
    SINKARRAY[i - 48].outputNumber = i;
    pinMode(i, OUTPUT);
  }
}

void testAllLeds(int testDelay) {
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      for (int k = 0; k < 6; k++) {
        ledOn(i, j, k);
        delay(testDelay);
        ledOff(i, j, k);
        closeAllSinks();
      }
    }
  }
}

////--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- SETUP AND TEST END////

void openAllSinks() {
  for (int i = 0; i < 6; i++) {
    digitalWrite(SINKARRAY[i].outputNumber, HIGH);
  }
}

void closeAllSinks() {
  for (int i = 0; i < 6; i++) {
    digitalWrite(SINKARRAY[i].outputNumber, LOW);
  }
}

void openSink(int sinkNumber) {
  digitalWrite(SINKARRAY[sinkNumber].outputNumber, HIGH);
}

void closeSink(int sinkNumber) {
  digitalWrite(SINKARRAY[sinkNumber].outputNumber, LOW);
}

////---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------.. SINKS END ////

void ledOn(int x, int y, int layer) {
  openSink(layer);
  digitalWrite(LEDARRAY[x][y][layer].outputNumber, HIGH);
}

void ledOff(int x, int y, int layer) {
  digitalWrite(LEDARRAY[x][y][layer].outputNumber, LOW);
}

void multipleLedsOn(GridPoint gridPoints[], int arraySize) {
  for (int i = 0; i < arraySize; i++) {
    ledOn(gridPoints[i].x, gridPoints[i].y, gridPoints[i].z);
  }
}

void multiLedsOff(GridPoint gridPoints[], int arraySize) {
  for (int i = 0; i < arraySize; i++) {
    ledOff(gridPoints[i].x, gridPoints[i].y, gridPoints[i].z);
  }
}

void allOn() {
  openAllSinks();
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      for (int k = 0; k < 6; k++) {
        ledOn(i, j, k);
      }
    }
  }
}

void allOff() {
  closeAllSinks();
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      for (int k = 0; k < 6; k++) {
        ledOff(i, j, k);
      }
    }
  }
}

////--------------------------------------------------------------------------------------------------------------------------------------------------------------------- DIGITAL ALL/SINGLE ON/OFF END ////

void layerOn(int layer) {
  openSink(layer);
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOn(i, j, layer);
    }
  }
}

void layerOff(int layer) {
  closeSink(layer);
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOff(i, j, layer);
    }
  }
}

void xLineOn(int pane, int layer) {
  openSink(layer);
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOn(i, pane, layer);
    }
  }
}

void xLineOff(int pane, int layer) {
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOff(i, pane, layer);
    }
  }
}

void yLineOn(int pane, int layer) {
  openSink(layer);
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOn(pane, i, layer);
    }
  }
}

void yLineOff(int pane, int layer) {
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOff(pane, i, layer);
    }
  }
}

void verticalLineOn(int x, int y) {
  openAllSinks();
  for (int i = 0; i < 6; i++) {
    ledOn(x, y, i);
  }
}

void verticalLineOff(int x, int y) {
  for (int i = 0; i < 6; i++) {
    ledOff(x, y, i);
  }
}

void xPaneOn(int pane) {
  openAllSinks();
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOn(i, pane, j);
    }
  }
}


void xPaneOff(int pane) {
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOff(i, pane, j);
    }
  }
}


void yPaneOn(int pane) {
  openAllSinks();
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOn(pane, i, j);
    }
  }
}


void yPaneOff(int pane) {
  for (int i = 0; i < 6; i++) {
    for (int j = 0; j < 6; j++) {
      ledOff(pane, i, j);
    }
  }
}

//// PANE AND EDGES ON/OFF END ////


void setup() {
  setupLedStructs();
  setupSinks();
  allOff();
  Serial.begin(9600);
  
}


void loop() {
  allOff();
  if (Serial.available() > 0) {
    DynamicJsonDocument jsonDocument<4000>;
    DeserializationError error = deserializeJson(jsonDocument, Serial);
    if (error == DeserializationError::Ok) {
      JsonArray stateArray = jsonDocument["states"].as<JsonArray>();
      for (int currentState = 0; currentState < stateArray.size(); currentState++) {
        JsonObject state = stateArray[currentState].as<JsonObject>();
        int duration = stateArray[currentState]["duration"];
        JsonArray poweredArray = state["powered"].as<JsonArray>();
        for (int currentLed = 0; currentLed < poweredArray.size(); currentLed++) {
          JsonObject led = poweredArray[currentLed].as<JsonObject>();
          int x = led["x"].as<int>();
          int y = led["y"].as<int>();
          int z = led["z"].as<int>();
          ledOn(x, y, z);
        }
        delay(duration);
        allOff(); 
      }
    } else {
      if (error = DeserializationError::NoMemory) {
        Serial.print("No memory ");
        size_t capacity = jsonDocument.capacity();
        Serial.println((int)capacity);
      } else {
        Serial.print("Error occurred.");
      }
    }
  }
 
  
}
