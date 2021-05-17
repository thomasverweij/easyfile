<script>
    import { createBucketAndLogin } from './api.js';
    import { tokenStore } from './store.js'

    export let bucketId;
    let password;
    let loading = false;

    async function setBucket() {
        loading = true;
        let bucket = await createBucketAndLogin(password)
            .finally(() => loading = false)
            .catch(() => alert("could not create bucket"))
        bucketId = bucket.id;
        tokenStore.set(bucket.token);
    }

</script>

<div id="create">
    <p>Create new bucket</p>
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