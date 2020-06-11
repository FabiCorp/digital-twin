
import React, { PureComponent, useState, useEffect } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const fetchCallCallback = (url, callback) => {
  fetch(url)
  .then(response => response.json())
  .then(array => {
    var responseArray = []
    array.forEach((element, index) => {
        responseArray.push(
          {
            name: "Value " + (index + 1),
            conditionChange: element.toFixed(2)
          })
    });
    callback(responseArray)
  });
}

const fetchAverageConditionList = setAverageConditionList => {
  fetchCallCallback("http://localhost:4465/averageCondition", setAverageConditionList)
}

function MyChart(props) {
  const [averageConditionList, setAverageConditionList] = useState([])

  const data = averageConditionList
  useEffect( _ => {
    console.log([[0, 3], [1, 1], [2, 5], [3, 6], [4, 4]])
    const fetchData = _ => {
        fetchAverageConditionList(setAverageConditionList)
    }
    fetchData()
    setInterval(fetchData, 5000)
},[])

  return (
    <LineChart width={600} height={300} data={data}>
      <XAxis dataKey="name"/>
      <YAxis type="number" domain={[1, 4]}/>
      <Tooltip />
      <Legend />
      <CartesianGrid stroke="#eee" strokeDasharray="5 5"/>
      <Line type="monotone" dataKey="conditionChange" stroke="#8884d8" strokeWidth={3}/>
  </LineChart>
  )
}

export default MyChart