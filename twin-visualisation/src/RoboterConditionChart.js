
import React, { PureComponent, useState, useEffect } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const fetchCallCallback = (url, callback) => {
  fetch(url)
  .then(response => {
    if (response.ok) {
      return response.json()
      }
    })
  .then(array => {
    if (array === undefined) return 
    var responseArray = []
    array.forEach((element, index) => {
        responseArray.push(
          {
            name: "Step " + index,
            condition: element.toFixed(2)
          })
    });
    callback(responseArray)
  });
}

const fetchCall = (url, callback) => {
  fetch(url)
  .then(response => {
    if (response.ok) {
      return response.json()
      }
    })
  .then(data => callback(data));
}

const fetchAverageConditionList = (setRobotConditionList, id) => {
  fetchCallCallback("http://localhost:4465/robotCondition/" + id, setRobotConditionList)
}

const fetchProductionRobotCount = setProductionRobotCount => {
  fetchCall("http://localhost:4465/robot/size", setProductionRobotCount)
}

const generateRobotIdOptions = idCount => {
  var optionsArray = []

  for (let index = 0; index < idCount; index++) {
    optionsArray.push(
      <option value={index}>{index}</option>
    )  
  }
  return optionsArray
}

const onChange = (event, callback, updateRobotChart) => {
  const robotId = event.target.value
  callback(robotId)
  updateRobotChart(robotId)
}

function MyChart(props) {
  const [robotConditionList, setRobotConditionList] = useState([])
  const [productionRobotCount, setProductionRobotCount] = useState(0)
  const [selectedRobot, setSelectedRobot] = useState(0)

  const updateRobotChart = id => fetchAverageConditionList(setRobotConditionList, id)

  const data = robotConditionList
  useEffect( _ => {
    console.log([[0, 3], [1, 1], [2, 5], [3, 6], [4, 4]])
    const fetchData = _ => {
        fetchProductionRobotCount(setProductionRobotCount)
    }
    fetchData()
    setInterval(fetchData, 5000)
},[])

  return (
    <>
    <select id="robot" onChange={ event => onChange(event, setSelectedRobot, updateRobotChart)}>
      {generateRobotIdOptions(productionRobotCount)}
    </select> 
    <LineChart width={600} height={300} data={data}>
      <XAxis dataKey="name" />
      <YAxis type="number" domain={[85, 101]}/>
      <Tooltip />
      <Legend />
      <CartesianGrid stroke="#eee" strokeDasharray="5 5"/>
      <Line type="monotone" dataKey="condition" stroke="#82ca9d" strokeWidth={3}/>
  </LineChart>
  </>
  )
}

export default MyChart