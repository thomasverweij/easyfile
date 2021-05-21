import { writable } from "svelte/store";
export const tokenStore = writable(localStorage.getItem("token") || "");
export const keyStore = writable(localStorage.getItem("key") || "");
export const messageStore = writable("");