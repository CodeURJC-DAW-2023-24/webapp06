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
  let texts = document.getElementById("inputTextAreaUpdate");
  texts.value = "";

  let id = document.getElementById("inputIdUpdate");
  id.value = postId;

  let postImage = document.getElementById("image_post");
  postImage.src = "/image/post/" + postId;

  try {
    const response = await fetch("/p/update/" + postId, {
      method: "GET",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.text();
    texts.value = data;
    const response2 = await fetch("/p/invalidate/" + postId, {
      method: "GET",
    });

    if (!response2.ok) {
      throw new Error("Network response was not ok");
    }
  } catch (error) {
    console.error("Error al obtener el texto: ", error);
  }
}

async function deletePost(button) {
  let postId = button.parentNode.getAttribute("data-id");

  const response = await fetch("/p/delete/" + postId + "/" + threadName, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  button.parentNode.parentNode.parentNode.style.display = "none";
}
