const THREAD_INDEX = 0;
const POST_INDEX = 1;

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
      data: [],
    },
    {
      label: "Weekly Posts",
      backgroundColor: "rgba(180, 180, 180, 0.2)",
      borderColor: "rgba(180, 180, 180, 1)",
      borderWidth: 1,
      data: [],
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

var chart = new Chart(ctx, {
  type: "bar",
  data: data,
  options: options,
});

setInitialWeekRange();
getWeekly(convertDateFormatBackend(formatDate(new Date())));

async function getAnnually(date) {
  document.getElementById("weeklyButton").className = "btn btn-outline-primary";
  document.getElementById("monthlyButton").className =
    "btn btn-outline-primary";
  document.getElementById("annuallyButton").className = "btn btn-primary";

  var weekPicker = document.getElementById("weekPicker");
  weekPicker.style.display = "none";

  try {
    const responseThread = await fetch(
      `https://localhost:8443/chart-rest/threads/annually?date=${date}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!responseThread.ok) {
      throw new Error("Network response was not ok");
    }

    const responsePost = await fetch(
      `https://localhost:8443/chart-rest/posts/annually?date=${date}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    if (!responsePost.ok) {
      throw new Error("Network response was not ok");
    }

    const dataThread = await responseThread.json();
    const dataPost = await responsePost.json();

    const years = Object.keys(dataThread).map(Number);
    const countsThread = Object.values(dataThread);
    const countsPost = Object.values(dataPost);

    chart.data.labels = years;

    chart.data.datasets[THREAD_INDEX].data = countsThread;
    chart.data.datasets[THREAD_INDEX].label = "Annually Threads";

    chart.data.datasets[POST_INDEX].data = countsPost;
    chart.data.datasets[POST_INDEX].label = "Annually Posts";

    chart.update();
  } catch (error) {
    console.error(error);
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

  const theadUrl = `https://localhost:8443/chart-rest/threads/weekly?date=${date}`;
  const postUrl = `https://localhost:8443/chart-rest/posts/weekly?date=${date}`;

  chart.data.labels = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  fetchDataAndUpdateChart(theadUrl, "Weekly Threads", THREAD_INDEX);
  fetchDataAndUpdateChart(postUrl, "Weekly Posts", POST_INDEX);
}

async function getMonthly(date) {
  document.getElementById("weeklyButton").className = "btn btn-outline-primary";
  document.getElementById("monthlyButton").className = "btn btn-primary";
  document.getElementById("annuallyButton").className =
    "btn btn-outline-primary";

  var weekPicker = document.getElementById("weekPicker");
  weekPicker.style.display = "none";

  const theadUrl = `https://localhost:8443/chart-rest/threads/monthly?date=${date}`;
  const postUrl = `https://localhost:8443/chart-rest/posts/monthly?date=${date}`;
  chart.data.labels = [
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

  fetchDataAndUpdateChart(theadUrl, "Weekly Threads", THREAD_INDEX);
  fetchDataAndUpdateChart(postUrl, "Weekly Posts", POST_INDEX);
}

async function fetchDataAndUpdateChart(url, label, index) {
  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const data = await response.json();
    updateChart(data, label, index);
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}

function updateChart(data, label, index) {
  chart.data.datasets[index].data = data;
  chart.data.datasets[index].label = label;
  chart.update();
}

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

function getDateWeek() {
  const weekRangeElement = document.getElementById("weekRange");
  const dates = weekRangeElement.textContent.split(" - ");
  const startDate = parseDate(dates[0]);

  startDate.setDate(startDate.getDate());

  startDateStr = formatDate(startDate);

  return convertDateFormatBackend(startDateStr);
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
