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

let iv = window.crypto.getRandomValues(new Uint8Array(12));

export let importRawKey = async (p) => {
    let enc = new TextEncoder();
    return window.crypto.subtle.importKey(
        "raw", 
        enc.encode(p), 
        {name: "PBKDF2"}, 
        false, 
        ["deriveBits", "deriveKey"]
    );
}

export let importJwkKey = async (k) => {
    return await window.crypto.subtle.importKey(
        "jwk",
        k,
        { "name": "AES-GCM", "length": 256},
        true,
        [ "encrypt", "decrypt" ]
    );
}
    
export let getKey = async (p) => {
    let salt = window.crypto.getRandomValues(new Uint8Array(16));
    let keyMaterial = await importRawKey(p);
    let key = await window.crypto.subtle.deriveKey(
        {
            "name": "PBKDF2",
            salt: salt, 
            "iterations": 100000,
            "hash": "SHA-256"
        },
        keyMaterial,
        { "name": "AES-GCM", "length": 256},
        true,
        [ "encrypt", "decrypt" ]
    )
    
    let exp = await window.crypto.subtle.exportKey("jwk", key)
    
    return btoa(JSON.stringify(exp, null, " "))
}

export let encryptData = async (d, k) => {
    return await window.crypto.subtle.encrypt({name: "AES-GCM", iv: iv}, k, d)
}

export let decryptData = async (d, k) => {
    return await window.crypto.subtle.decrypt({ name: "AES-GCM", iv: iv }, k, d)
}

export let encryptText = async (d, k) => {
    return await encryptData(new TextEncoder().encode(d), k)
}

export let decryptText = async (d, k) => {
    return new TextDecoder().decode(await decryptData(d, k))
}

export let readFileEncrypted = (file, key) => {
    return new Promise((resolve, reject) => {
        let reader = new FileReader()
        reader.onload = () => {
          resolve(encryptData(reader.result, key))
        };
        reader.onerror = reject;
        reader.readAsArrayBuffer(file);
    })
}

// export const fromHexString = hexString =>
//   new Uint8Array(hexString.match(/.{1,2}/g).map(byte => parseInt(byte, 16)));

// export const toHexString = bytes =>
//   bytes.reduce((str, byte) => str + byte.toString(16).padStart(2, '0'), '');


// export let getPBKDF2Key = async (p) => {
//     let salt;
//     const keyMaterial = await importRawKey(p);
//     salt = window.crypto.getRandomValues(new Uint8Array(16));
//     const derivedBits = await window.crypto.subtle.deriveBits(
//       {
//         "name": "PBKDF2",
//         salt: salt,
//         "iterations": 100000,
//         "hash": "SHA-256"
//       },
//       keyMaterial,
//       256
//     );
//     return btoa(toHexString(new Uint8Array(derivedBits,0,16)))
// }

export let encryptFile = async (file, key) => {
    let k = await importJwkKey(JSON.parse(atob(key)))
    return new File(
        [await readFileEncrypted(file, k)], 
        file.name, 
        {"type": file.type}
    )
}

export let decryptFile = async (buffer, key) => {
    let k = await importJwkKey(JSON.parse(atob(key)))
    return await decryptData(buffer, k)
}