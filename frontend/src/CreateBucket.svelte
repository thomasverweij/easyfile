<script>
    import { createBucketAndLogin } from './api.js';
    import { tokenStore } from './store.js';
    import { notify } from './utils';
    import { fade } from 'svelte/transition';


    export let bucketId;
    let password;
    let hideCreate = true;
    let loading = false;


    async function setBucket() {
        let bucket = await createBucketAndLogin(password)
            .then(() => notify("Bucket created"))
            .catch(() => notify("could not create bucket"))
            .finally(() => loading = true)
        bucketId = bucket.id;
        tokenStore.set(bucket.token);
    }

</script>
<div id="welcome">
    Create disposable buckets for sharing multiple files. 
    <ul>
        <li>5GB file size limit.</li>
        <li>Files are end-to-end encrypted.</li>
        <li>Buckets are automatically deleted after 24 hours.</li>
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
    margin: 0 auto;
    padding: 30px;
    box-shadow: 10px 10px rgb(77, 77, 77);
}

#welcome {
    width: 50%;
    margin: 0 auto 30px;
    padding: 10px 0 10px 150px;
    background-image: url("/welcome.svg");
    background-repeat: no-repeat;
}

@media (max-width: 500px) {
    #welcome {
        width: 80%;
        margin: 0 auto 50px;
        padding: 150px 0 0 0;
        background-image: url("/welcome.svg");
        background-repeat: no-repeat;
        background-position: center top;
    } 
}



.password {
    width: 60%;
}

.go {
    text-align: center;
    width: 50%;
    margin: 0 auto;
    padding: 30px;
    /* background-color: #e1e1e1; */
}

.startbutton {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: #E9E9E9;
    box-shadow: 10px 10px rgb(77, 77, 77);
}
  
</style>