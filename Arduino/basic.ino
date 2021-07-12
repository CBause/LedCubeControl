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

////------------------------------------------------------------------------------------------------------------------------------------------------------------------------- PANE AND EDGES ON/OFF END ////


void layerRun(int runDelay) {
  for (int i = 0; i < 6; i++) {
    layerOn(i);
    delay(runDelay);
    layerOff(i);
  }
  for (int i = 5; i >= 0; i--) {
    layerOn(i);
    delay(runDelay);
    layerOff(i);
  }
}

void layerRunReverse(int runDelay) {
  for (int i = 5; i >= 0; i--) {
    layerOn(i);
    delay(runDelay);
    layerOff(i);
  }

  for (int i = 0; i < 6; i++) {
    layerOn(i);
    delay(runDelay);
    layerOff(i);
  }
}

void xPaneRun(int runDelay) {
  for (int i = 0; i < 6; i++) {
    xPaneOn(i);
    delay(runDelay);
    xPaneOff(i);
  }
  for (int i = 5; i >= 0; i--) {
    xPaneOn(i);
    delay(runDelay);
    xPaneOff(i);
  }
}

void xPaneRunReverse(int runDelay) {
  for (int i = 5; i >= 0; i--) {
    xPaneOn(i);
    delay(runDelay);
    xPaneOff(i);
  }
  for (int i = 0; i < 6; i++) {
    xPaneOn(i);
    delay(runDelay);
    xPaneOff(i);
  }
}

void yPaneRun(int runDelay) {
  for (int i = 0; i < 6; i++) {
    yPaneOn(i);
    delay(runDelay);
    yPaneOff(i);
  }
  for (int i = 5; i >= 0; i--) {
    yPaneOn(i);
    delay(runDelay);
    yPaneOff(i);
  }
}

void yPaneRunReverse(int runDelay) {
  for (int i = 5; i >= 0; i--) {
    yPaneOn(i);
    delay(runDelay);
    yPaneOff(i);
  }
  for (int i = 0; i < 6; i++) {
    yPaneOn(i);
    delay(runDelay);
    yPaneOff(i);
  }
}

////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- PANE RUNS END ////

void verticalLineRun(int delayTime, int runLength) {
  int currentX = 0;
  int currentY = 0;
  for (int i = 0; i < runLength; i++) {
    verticalLineOn(currentX, currentY);
    delay(delayTime);
    bool validDirection = false;
    int nextDirection = -1;
    while(!validDirection) {
      validDirection = true;
      nextDirection = random(4);
      if ((nextDirection == 0 && currentY == 5) || (nextDirection == 1 && currentX == 0) || (nextDirection == 2 && currentY == 0) || (nextDirection == 3 && currentX == 5)) {
        validDirection = false;
      }
    }
    verticalLineOff(currentX, currentY);
    switch(nextDirection) {
      case 0: currentY++; break;
      case 1: currentX--; break;
      case 2: currentY--; break;
      case 3: currentX++; break;
    }
  }
}

////------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- LINE RUNS END ////

void fillAndDrainVertical(int fillDelay, int drainDelay) {
  int delayTime = fillDelay;
  for (int j = 1; j <= 2; j++) {
    if (j == 2)
      delayTime = drainDelay;
    for (int i = 0; i < 6; i++) {
      verticalLineOn(0,i);
      delay(delayTime);  
    }
    for (int i = 1; i < 6; i++) {
      verticalLineOn(i,5);
      delay(delayTime);  
    }
    for (int i = 4; i >= 0; i--) {
      verticalLineOn(5,i);
      delay(delayTime); 
    }
    for (int i = 4; i > 0; i--) {
      verticalLineOn(i,0);
      delay(delayTime);  
    }
    for (int i = 0; i < 6; i++) {
      verticalLineOff(0,i);
      delay(delayTime);  
    }
    for (int i = 1; i < 6; i++) {
      verticalLineOff(i,5);
      delay(delayTime);  
    }
    for (int i = 4; i >= 0; i--) {
      verticalLineOff(5,i);
      delay(delayTime); 
    }
   for (int i = 4; i > 0; i--) {
      verticalLineOff(i,0);
      delay(delayTime);  
    }
  }
}

void edgeCube() {
  xLineOn(0, 0);
  xLineOn(0, 5);
  xLineOn(5, 0);
  xLineOn(5, 5);
  yLineOn(0, 0);
  yLineOn(0, 5);
  yLineOn(5, 0);
  yLineOn(5, 5);
  delay(5);
  yLineOff(0, 0);
  yLineOff(0, 5);
  yLineOff(5, 0);
  yLineOff(5, 5);
  xLineOff(0, 0);
  xLineOff(0, 5);
  xLineOff(5, 0);
  xLineOff(5, 5);
  closeAllSinks();
  verticalLineOn(0, 0);
  verticalLineOn(0, 5);
  verticalLineOn(5, 0);
  verticalLineOn(5, 5);
  delay(5);
  verticalLineOff(0, 0);
  verticalLineOff(0, 5);
  verticalLineOff(5, 0);
  verticalLineOff(5, 5);
  closeAllSinks();
}

void randomInterval(int switches, int switchDelay) {
  for (int i = 0; i < switches; i++) {
    int x = random(6);
    int y = random(6);
    int z = random(6);
    ledOn(x, y, z);
    delay(switchDelay);
    ledOff(x, y, z);
    layerOff(z);
  }
}

void paneBlinkBlitz(int delayTime) {

  layerRun(50);
  layerRun(50);
  layerRun(50);
  xPaneRun(50);
  xPaneRun(50);
  xPaneRun(50);
  yPaneRun(50);
  yPaneRun(50);
  yPaneRun(50);
  allOff();
  delay(100);
  allOn();
  delay(100);
  allOff();
  delay(100);
  allOn();
  delay(100);
  allOff();
  delay(100);
  allOn();
  delay(100);
  allOff();
  randomInterval(200, 50);
}

void randomOnOrOff(int delayTime) {
  int onOrOff = random(2);
  int x = random(6);
  int y = random(6);
  int z = random(6);
  if (onOrOff == 0) {
    ledOn(x,y,z);
  } else {
    ledOff(x,y,z);
  }
  delay(delayTime);
}

void randomOnOutsidePane() {
  int x = random(6);
  int y = random(6);
  int atEnd = random(2);
  if (atEnd == 1)
    atEnd = 5;
  int zeroPane = random(3);
  if (zeroPane == 0) {
    ledOn(atEnd, x, y);
    delay(100);
    ledOff(atEnd, x, y);
  }
  if (zeroPane == 1) {
    ledOn(x, atEnd, y);
    delay(100);
    ledOff(x, atEnd, y);
  }
  if (zeroPane == 2) {
    ledOn(y, x, atEnd);
    delay(100);
    ledOff(y, x, atEnd);
  }
}

GridPoint getRandomLedOnOutsidePane() {
  GridPoint result;
  int a = random(6);
  int b = random(6);
  int atEnd = (2);
  if (atEnd == 1)
    atEnd = 5;
  int whichPaneZero = random(3);
  if (whichPaneZero == 0) {
    result.x = atEnd;
    result.y = a;
    result.z = b;
  } else if (whichPaneZero == 1) {
    result.x = a;
    result.y = atEnd;
    result.z = b;
  } else {
    result.x = a;
    result.y = b;
    result.z = atEnd;
  }
  return result;
}

void singleRunningLed(int delayTime) {
  GridPoint gridPoint = getRandomLedOnOutsidePane();
  ledOn(gridPoint.x, gridPoint.y, gridPoint.z);
}


void setup() {
  setupLedStructs();
  setupSinks();
  allOff();
  Serial.begin(9600);
}

void loop() {
  allOn();
}
