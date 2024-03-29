package com.taxifleet.server.network.socket;

/**
 * Threads de escuta na rede. Ex.: uma thread aguardando o recebimento de
 * mensagens ShoutMessage para ent�o responder imediatamente;
 * 
 * @author Thomas
 * 
 */
public abstract class Listener implements Runnable {

	protected long time;

	public Listener(long time) {
		this.time = time;
	}

	@Override
	public void run() {
		try {
			while (true) {
				listen();
			}
		} catch (InterruptedException e) {
			// TODO LOG PARA LISTENERTIME
			e.printStackTrace();
		}
	}

	/**
	 * Comportamento da resposta desta thread
	 */
	public abstract void listen() throws InterruptedException;

}
