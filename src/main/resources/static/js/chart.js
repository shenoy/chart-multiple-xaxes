console.log("IN CHART JS FILE");
var api ="/data";


const labels = Object.keys(chartData.intMap1);
const values1 = Object.values(chartData.intMap1);
const values2 = Object.values(chartData.intMap2);


console.log(labels, "=======labels");
console.log(values1, "=======labels");
console.log(values2, "=======labels");

console.log()

  const data = {
    labels: labels,
    datasets: [{
      label: 'My First dataset',
      backgroundColor: 'rgb(255, 99, 132)',
      borderColor: 'rgb(255, 99, 132)',
      data: values1,
      tension: 0.1
    },

    {
      label: 'My Second dataset',
     backgroundColor: 'rgb(0, 0, 0)',
      borderColor: 'rgb(0, 0, 0)',
      data: values2,
      tension: 0.5
        }
    ]
  };

  const config = {
    type: 'line',
    data: data,
    options: {
    scales: {
    y: {
      min: -10,
      max: 50,
    }}
  }
  };


  const myChart = new Chart(
      document.getElementById('myChart'),
      config
    );