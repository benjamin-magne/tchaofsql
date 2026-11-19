package net.unotec.app.commands

import net.unotec.app.services.SessionService

class CliContext(val sessionService: SessionService = SessionService()) {}