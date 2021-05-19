<script>
    import { createBucketAndLogin } from './api.js';
    import { tokenStore } from './store.js';
    import { notify } from './utils';

    export let bucketId;
    let password;
    let loading = false;

    async function setBucket() {
        let bucket = await createBucketAndLogin(password)
            .finally(() => loading = false)
            .catch(() => notify("could not create bucket"))
            notify("Bucket created")
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
<div id="create">
    <p>New bucket:</p>
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

{#if loading}
<div id="loading"><p>creating bucket...</p></div>
{/if}

<style>
#create {
    border-radius: 25px;
    text-align: center;
    width: 50%;
    margin: 0 auto;
    padding: 30px;
    background-color: #e1e1e1;
}

#welcome {
    width: 50%;
    margin: 0 auto;
    padding: 30px;
}

#loading {
    animation: toVisible 0s 1s forwards;
    visibility: hidden;
  }

.password {
    width: 60%;
}
  
  @keyframes toVisible {
    to   { visibility: visible; }
  }
</style>