<script>
	import CreateBucket from "./CreateBucket.svelte";
	import Bucket from "./Bucket.svelte"
	import BucketLogin from "./BucketLogin.svelte"
	import { onMount } from 'svelte';
	import { getBucket, getToken, createBucket } from './api.js';

	let token = localStorage.getItem('token') || undefined;
	let bucketId = window.location.hash.split('#')[1] || undefined;

	$: if(token) {
		localStorage.setItem('token', token);
	}

</script>
<main>
	<h2 class="title"><a href="/">EasyFile</a></h2>
	{#if !bucketId && !token}
		<CreateBucket bind:bucketId={bucketId} bind:token={token} />
	{:else if !token}
		<BucketLogin bind:bucketId={bucketId} bind:token={token} />
	{:else}
		<Bucket bucketId={bucketId} token={token} />
	{/if}
</main>

<style>
	:global(body) {
		background-color: rgb(233, 233, 233);
		color:rgb(97, 97, 97);
	}

	:global(input) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	:global(button) {
		background-color:  rgb(233, 233, 233);
		border: none;
	}

	h2 {
		text-align: center;
		color: rgb(107, 107, 107);
		margin: 40px;
	}

	a {
  		color:inherit;
  		text-decoration: none;
	}
	 
	main {
		max-width: 650px;
		margin: 0 auto;
	}


	@media (max-width: 700px) {
		main {
			max-width: none;
		}
	}
</style>