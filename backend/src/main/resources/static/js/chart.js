var data = {
  labels: [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ],
  datasets: [
    {
      label: "Weekly Posts",
      backgroundColor: "rgba(75, 192, 192, 0.2)",
      borderColor: "rgba(75, 192, 192, 1)",
      borderWidth: 1,
      data: getWeekly(convertDateFormatBackend(formatDate(new Date()))),
    },
  ],
};

var options = {
  scales: {
    y: {
      beginAtZero: true,
    },
  },
};

var ctx = document.getElementById("statics").getContext("2d");
var myChart = new Chart(ctx, {
  type: "bar",
  data: data,
  options: options,
});

async function getMonthly(date) {
  document.getElementById("monthlyButton").className = "btn btn-primary";
  document.getElementById("weeklyButton").className = "btn btn-outline-primary";
  document.getElementById("annuallyButton").className =
    "btn btn-outline-primary";

  var weekPicker = document.getElementById("weekPicker");
  weekPicker.style.display = "none";

  try {
    const response = await fetch(
      `https://localhost:8443/chart-rest/threads/monthly?date=${date}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();

    myChart.data.datasets[0].data = data;
    myChart.data.labels = [
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December",
    ];
    myChart.data.datasets[0].label = "Monthly Threads";

    myChart.update();
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}

async function getWeekly(date) {
  document.getElementById("weeklyButton").className = "btn btn-primary";
  document.getElementById("monthlyButton").className =
    "btn btn-outline-primary";
  document.getElementById("annuallyButton").className =
    "btn btn-outline-primary";

  var weekPicker = document.getElementById("weekPicker");
  weekPicker.style.display = "block";

  try {
    const response = await fetch(
      `https://localhost:8443/chart-rest/threads/weekly?date=${date}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();

    myChart.data.datasets[0].data = data;
    myChart.data.labels = [
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
      "Sunday",
    ];
    myChart.data.datasets[0].label = "Weekly Threads";

    myChart.update();
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}

async function getAnnually(date) {
  document.getElementById("weeklyButton").className = "btn btn-outline-primary";
  document.getElementById("monthlyButton").className =
    "btn btn-outline-primary";
  document.getElementById("annuallyButton").className = "btn btn-primary";

  var weekPicker = document.getElementById("weekPicker");
  weekPicker.style.display = "none";

  try {
    const response = await fetch(
      `https://localhost:8443/chart-rest/threads/annually?date=${date}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();

    const years = Object.keys(data).map(Number);
    const counts = Object.values(data);

    myChart.data.datasets[0].data = counts;
    myChart.data.labels = years;
    myChart.data.datasets[0].label = "Annually Threads";

    myChart.update();
  } catch (error) {
    console.error(error);
  }
}

document.addEventListener("DOMContentLoaded", function () {
  setInitialWeekRange();
});

function setInitialWeekRange() {
  const today = new Date();
  const dayOfWeek = today.getDay();
  const startOffset = (dayOfWeek === 0 ? -6 : 1) - dayOfWeek;
  const startDate = new Date(today.setDate(today.getDate() + startOffset));
  const endDate = new Date(startDate.getTime());
  endDate.setDate(endDate.getDate() + 6);

  document.getElementById("weekRange").textContent =
    formatDate(startDate) + " - " + formatDate(endDate);
}

function changeWeek(direction) {
  const weekRangeElement = document.getElementById("weekRange");
  const dates = weekRangeElement.textContent.split(" - ");
  const startDate = parseDate(dates[0]);
  const endDate = parseDate(dates[1]);

  startDate.setDate(startDate.getDate() + direction * 7);
  endDate.setDate(endDate.getDate() + direction * 7);

  startDateStr = formatDate(startDate);

  getWeekly(convertDateFormatBackend(startDateStr));

  weekRangeElement.textContent = startDateStr + " - " + formatDate(endDate);
}

function parseDate(dateString) {
  const parts = dateString.split("/");
  return new Date(parts[2], parts[1] - 1, parts[0]);
}

function formatDate(date) {
  return (
    date.getDate().toString().padStart(2, "0") +
    "/" +
    (date.getMonth() + 1).toString().padStart(2, "0") +
    "/" +
    date.getFullYear()
  );
}

function convertDateFormatBackend(dateString) {
  const parts = dateString.split("/");
  const formattedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;

  return formattedDate;
}
