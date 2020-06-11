import React, { useEffect, useState } from 'react'
import './ThingNumberChart.css';

const fetchCall = (url, callback) => {
    fetch(url   )
    .then(response => response.json())
    .then(data => callback(data));
}

const fetchProductionFlowCount = setProductionFlowCount => {
    fetchCall("http://localhost:4465/workflow/size", setProductionFlowCount)
}

const fetchProductionPlaceCount = setProductionPlaceCount => {
    fetchCall("http://localhost:4465/place/size", setProductionPlaceCount)
}

const fetchProductionRobotCount = setProductionRobotCount => {
    fetchCall("http://localhost:4465/robot/size", setProductionRobotCount)
}

const fetchRepairedRobotsCount = setRepairedRobotsCount => {
    fetchCall("http://localhost:4465/repairedRobots", setRepairedRobotsCount)
}

function TwinInformation() {
  
    const [productionFlowCount, setProductionFlowCount] = useState(0)
    const [productionPlaceCount, setProductionPlaceCount] = useState(0)
    const [productionRobotCount, setProductionRobotCount] = useState(0)
    const [repairedRobotsCount, setRepairedRobotsCount] = useState(0)

    useEffect( _ => {

        const fetchData = _ => {
            console.log("DOIT")
            fetchProductionFlowCount(setProductionFlowCount)
            fetchProductionPlaceCount(setProductionPlaceCount)
            fetchProductionRobotCount(setProductionRobotCount)
            fetchRepairedRobotsCount(setRepairedRobotsCount)
        }
        fetchData()
        setInterval(fetchData, 2000)
    },[])

    return (
        <div className="twin-information-container">
            <div className="twin-information-top">
                <div className="twin-information-counter">
                    Production Work Flow Count: {productionFlowCount}
                </div>
                <div className="twin-information-counter">
                    Production Place Count: {productionPlaceCount}
                </div>
            </div>
        
            <div className="twin-information-bottom">
                <div className="twin-information-counter">
                    Production Robot Count: {productionRobotCount}
                </div>
                <div className="twin-information-counter">
                    Repaired Robots: {repairedRobotsCount}
                </div>
            </div>
        
        </div>
    )
}

export default TwinInformation