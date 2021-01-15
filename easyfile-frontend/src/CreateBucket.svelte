<script>
import { createBucketAndLogin } from './api.js';

    export let bucketId;
    export let token;
    let password;
    let loading = false;

    async function setBucket() {
        loading = true;
        let bucket = await createBucketAndLogin(password);
        bucketId = bucket.id;
        token = bucket.token;
    }

</script>

<p>Create new bucket:</p>
<p>
    <input type="password" placeholder="password" bind:value={password} />
    <button on:click={setBucket} disabled={loading}>Ok</button>
</p>
{#if loading}
    <div id="loading"><p>creating bucket...</p></div>
{/if}

<style>
#loading {
    animation: toVisible 0s 1s forwards;
    visibility: hidden;
  }
  
  @keyframes toVisible {
    to   { visibility: visible; }
  }
</style>