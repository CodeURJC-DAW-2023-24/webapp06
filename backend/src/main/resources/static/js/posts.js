async function reportPost(button) {
  let postId = button.parentNode.getAttribute("data-id");

  if (!button.querySelector("i").classList.contains("disliked")) {
    const response = await fetch("/p/report/" + postId, {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const result = await response.json();

    if (result) {
      button.querySelector("i").classList.add("disliked");
    }
  }
}

async function editPost(button) {
  let postId = button.parentNode.getAttribute("data-id");

  const response = await fetch("/p/update/" + postId, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
}

async function deletePost(button) {
  let postId = button.parentNode.getAttribute("data-id");
  let thread = button.parentNode.getAttribute("thread-name");

  //"/t/" + thread + "/deletePost/" + postId
  //"/p/delete/" + postId + "/" + thread
  const response = await fetch("/p/delete/" + postId + "/" + thread, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  button.parentNode.parentNode.parentNode.style.display = "none";
}
