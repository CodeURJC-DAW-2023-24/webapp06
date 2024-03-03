async function toggleLike(likesButton) {
  let likeIcon = likesButton.querySelector("#likeIcon");
  let likeText = likesButton.querySelector("#likeText");

  let isLiked = likesButton.getAttribute("data-liked") === "true";

  if (isLiked) {
    likesButton.setAttribute("data-liked", "false");
    likeIcon.classList.remove("liked"); // Remove the 'liked' class
    likeText.textContent = parseInt(likeText.textContent) - 1; // Decrease likes count
  } else {
    likesButton.setAttribute("data-liked", "true");
    likeIcon.classList.add("liked"); // Add the 'liked' class
    likeText.textContent = parseInt(likeText.textContent) + 1; // Increase likes count
  }
}

async function toggleDislike(dislikesButton) {
  let dislikeIcon = dislikesButton.querySelector("#dislikeIcon");
  let dislikeText = dislikesButton.querySelector("#dislikeText");

  let isDisliked = dislikesButton.getAttribute("data-disliked") === "true";

  if (isDisliked) {
    dislikesButton.setAttribute("data-disliked", "false");
    dislikeIcon.classList.remove("disliked"); // Remove the 'liked' class
    dislikeText.textContent = parseInt(dislikeText.textContent) - 1; // Decrease likes count
  } else {
    dislikesButton.setAttribute("data-disliked", "true");
    dislikeIcon.classList.add("disliked"); // Add the 'liked' class
    dislikeText.textContent = parseInt(dislikeText.textContent) + 1; // Increase likes count
  }
}
