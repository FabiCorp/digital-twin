import React, { useEffect, useState } from 'react';
import './App.css';
import RoboterConditionChart from './RoboterConditionChart';
import AverageChangeChart from './AverageChangeChart';
import TotalTimeChart from './TotalTimeChart';
import ThingNumberChart from './ThingNumberChart';

const fetchCall = url => {
  fetch(url)
}


const initTwinServer = _ => {
  fetchCall("http://localhost:4465/init")
}

const initEntitiyServer = _ => {
  fetchCall("http://localhost:4460/init")
}

const processStep = _ => {
  fetchCall("http://localhost:4460/process")
}


function App() {

  return (
    <div className="App">
      <div className="Top-Bar"/>
       
      <div className="Title">
          Digital Twin Visual Interface
      </div>
      <div className="Server-Buttons"> 
        <button className="Start-Twin-Server" onClick={initTwinServer}>
          Initialize Twin Server
        </button>
        <button className="Start-Entity-Server"  onClick={initEntitiyServer}>
          Initialize Entity Server
        </button>
        <button className="ProcessStep" onClick={processStep}>
          Process Step
        </button>
      </div>

      <div className="Production-Numbers">
        <div className="Chart-Text">
          Thing Information
        </div>
        <ThingNumberChart/>
      </div>
      <div className="Total-Time">
        <div className="Chart-Text">
          Total Production Time Progress
        </div>
        <TotalTimeChart/>
      </div>
      <div className="Roboter-Condition">
        <div className="Chart-Text">
          Roboter Condition Process
        </div>
      
        <RoboterConditionChart/>
      </div>
      
      <div className="Average-Change">
        <div className="Chart-Text">
          Average Condition Decrease
        </div>
        <AverageChangeChart/>
      </div>

    </div>
  );
}

export default App;
