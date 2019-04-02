#!/bin/sh

set -e

scriptdir=$(dirname $(readlink -f "$0"))
cd "${scriptdir}"

exec java -cp ".:lib/*" eu.asdtech.tradelistener.App
