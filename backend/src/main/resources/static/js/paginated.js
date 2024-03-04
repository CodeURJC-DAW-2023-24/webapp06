var page = 0;

async function addNewElements(createHTML, url, buttonId, containerID) {
  showSpinner(true);

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

  page += 1;
  if (page >= data.totalPages) {
    document.getElementById(buttonId).style.display = "none";
  } else {
    document.getElementById(buttonId).style.display = "block";
  }

  const container = document.getElementById(containerID);

  showSpinner(false);

  data.content.forEach((it) => {
    container.insertAdjacentHTML("beforeend", createHTML(it));
  });
}
