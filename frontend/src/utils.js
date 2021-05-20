import { messageStore } from './store.js'

export function timeSince(date) {

    date = Date.parse(date)
    let seconds = Math.floor((new Date() - date) / 1000);
  
    let interval = seconds / 31536000;
  
    if (interval > 1) {
      return Math.floor(interval) + " years";
    }
    interval = seconds / 2592000;
    if (interval > 1) {
      return Math.floor(interval) + " months";
    }
    interval = seconds / 86400;
    if (interval > 1) {
      return Math.floor(interval) + " days";
    }
    interval = seconds / 3600;
    if (interval > 1) {
      return Math.floor(interval) + " hours";
    }
    interval = seconds / 60;
    if (interval > 1) {
      return Math.floor(interval) + " minutes";
    }
    return Math.max(Math.floor(seconds), 0) + " seconds";
  }

export function deletionCountDown(now, date, days) {
    let deletionDate = new Date(date)

    deletionDate.setDate(deletionDate.getDate() + days)

    let distance = Math.max(deletionDate.getTime() - now.getTime(), 0)

    let time = [
      Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
      Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60)),
      Math.floor((distance % (1000 * 60)) / 1000)
    ].map(t => {
      return t.toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      })
    })
    return "(" + time[0] + ":" + time[1] + ":" + time[2] + ")"
}

export function parseJwt(token) {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

export function notify(message) {
  messageStore.set({message})
}
