<script>
    import { createBucketAndLogin } from './api.js';
    import { keyStore, tokenStore } from './store.js';
    import { getKey, notify } from './utils';
    import { fade } from 'svelte/transition';


    export let bucketId;
    let password;
    let hideCreate = true;
    let loading = false;

    let setBucket = async () => {
        await createBucketAndLogin(password)
            .then(async (r) => {
                await getKey(password).then((k) => {
                    keyStore.set(k)
                })
                tokenStore.set(r.token);
                bucketId = r.id;
                loading = true
                notify("Bucket created")
            }).catch(() => notify("Could not create bucket"))
        }
</script>

<div id="welcome">
    Create private disposable buckets for sharing multiple files. 
    <ul>
        <li>Files and metadata are end-to-end encrypted in the browser.</li>
        <li>Buckets are automatically deleted after 24 hours.</li>
        <li>500Mb file size limit (due to browser encryption).</li>
    </ul>
</div>

{#if hideCreate}
    <div class="go">
        <button class="startbutton" on:click={() => hideCreate = !hideCreate}>Start</button>
    </div>
{:else}
    <div id="create" class:invisible={hideCreate} in:fade>
        <p>Create a password:</p>
        <p>
            <input 
                type="password"
                class="password"
                placeholder="password" 
                bind:value={password} 
                on:keyup|preventDefault={(e) => e.code == 'Enter' ? setBucket() : false}
            />
            <button on:click={setBucket} disabled={loading}>Ok</button>
        </p>
    </div>
{/if}

<style>


#create {
    border: 2px solid #e1e1e1;
    border-radius: 25px;
    text-align: center;
    width: 50%;
    margin: 0 auto 50px auto;
    padding: 30px;
    box-shadow: 10px 10px rgb(77, 77, 77);
}

#welcome {
    width: 50%;
    margin: 0 auto 50px auto;
    padding: 10px 0 10px 150px;
    background-image: url("/welcome.svg");
    background-repeat: no-repeat;
}

li {
    margin-bottom: 10px;
}

@media (max-width: 500px) {
    #welcome {
        width: 80%;
        padding: 150px 0 0 0;
        background-image: url("/welcome.svg");
        background-repeat: no-repeat;
        background-position: center top;
    } 

    #create {
        width: 80%;
        padding: 10px;
        box-shadow: 10px 10px rgb(77, 77, 77);
    }
}



.password {
    width: 60%;
}

.go {
    text-align: center;
    width: 50%;
    margin: 0 auto;
}

.startbutton {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: #E9E9E9;
    box-shadow: 10px 10px rgb(77, 77, 77);
}
  
</style>