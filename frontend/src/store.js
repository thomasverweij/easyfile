import { writable } from "svelte/store";
export const tokenStore = writable(localStorage.getItem("token") || "");
export const messageStore = writable("");