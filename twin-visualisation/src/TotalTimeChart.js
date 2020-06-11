
import React, { PureComponent, useState, useEffect } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';


const secondFetchCallCallback = (url, callback, firstArray) => {
  fetch(url)
  .then(response =>{
    if (response.ok) {
      return response.json()
      }
    })
  .then(array => {
    if (array === undefined) return
    array.forEach((element, index) => {
       firstArray[index].workflowTwo = element.toFixed(2)
    });
    callback(firstArray)
  });
}
const fetchCallCallback = (url, callback) => {
  fetch(url)
  .then(response => {
    if (response.ok) {
      return response.json()
      }
    })
  .then(array => {
    if (array !== undefined) {
      var responseArray = []
      array.forEach((element, index) => {
          responseArray.push(
            {
              name: "Step " + index,
              workflowOne: element.toFixed(2)
            })
      });
      secondFetchCallCallback("http://localhost:4465/totalTimeList/1", callback, responseArray)
    }
  });
}

const fetchtotalTimeListOneList = setAverageConditionList => {
  fetchCallCallback("http://localhost:4465/totalTimeList/0", setAverageConditionList)
}

function MyChart(props) {
  const [averageConditionList, setAverageConditionList] = useState([])

  const data = averageConditionList
  useEffect( _ => {
    console.log([[0, 3], [1, 1], [2, 5], [3, 6], [4, 4]])
    const fetchData = _ => {
      fetchtotalTimeListOneList(setAverageConditionList)
    }
    fetchData()
    setInterval(fetchData, 5000)
},[])

  return (
    <LineChart width={600} height={300} data={data}>
      <XAxis dataKey="name" />
      <YAxis type="number" domain={[0, 300]}/>
      <Tooltip />
      <Legend />
      <CartesianGrid stroke="#eee" strokeDasharray="5 5"/>
      <Line type="monotone" dataKey="workflowOne" stroke="#8884d8" strokeWidth={3}/>
      <Line type="monotone" dataKey="workflowTwo" stroke="#82ca9d" strokeWidth={3}/>
  </LineChart>
  )
}

export default MyChart