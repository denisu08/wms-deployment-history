import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import { Link } from 'react-router-dom';
import moment from 'moment';
import AppNavbar from './AppNavbar';

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {groups: [], isLoading: true, startTime: moment().subtract(1, 'd').format('YYYY-MM-DDTHH:mm'), endTime: moment().format('YYYY-MM-DDTHH:mm')};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('api/groups')
            .then(response => response.json())
            .then(data => this.setState({groups: data, isLoading: false}));
    }

    handleChange(moment) {
        this.setState({
            moment
        });
    }

    async remove(id) {
        await fetch(`/api/group/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedGroups = [...this.state.groups].filter(i => i.id !== id);
            this.setState({groups: updatedGroups});
        });
    }

    render() {
        const {groups, isLoading, startTime, endTime} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const shortcuts = {
            'Today': moment(),
            'Yesterday': moment().subtract(1, 'days'),
            'Clear': ''
        };

        const groupList = groups.map(group => {
            const address = `${group.address || ''} ${group.city || ''} ${group.stateOrProvince || ''}`;
            return <tr key={group.id}>
                <td style={{whiteSpace: 'nowrap'}}>{group.name}</td>
                <td>{address}</td>
                <td>{group.events.map(event => {
                    return <div key={event.id}>{new Intl.DateTimeFormat('en-US', {
                        year: 'numeric',
                        month: 'long',
                        day: '2-digit'
                    }).format(new Date(event.date))}: {event.title}</div>
                })}</td>
                <td>test1</td>
                <td>test2</td>
                <td>test3</td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <ButtonGroup>
                            <Button color="secondary" onClick={() => console.log("Export")}>Export Bundle</Button>
                            <Button color="info" onClick={() => console.log("Import")}>Import Bundle</Button>
                        </ButtonGroup>
                    </div>
                    <h3>Deployment History</h3>
                    <hr/>
                    <div>
                        <form>
                            <div className="form-group row">
                                <label htmlFor="inputPassword" className="col-sm-2 col-form-label">Filter Date</label>
                                <div className="col-sm-4">
                                    <input type="datetime-local" className="form-control" id="endTime" placeholder="End Time" value={startTime}></input>
                                </div>
                                <div className="col-sm-4">
                                    <input type="datetime-local" className="form-control" id="endTime" placeholder="End Time" value={endTime}></input>
                                </div>
                                <div className="col-sm-2">
                                    <button type="submit" className="btn btn-primary">Search</button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="15%">Modified Date</th>
                            <th width="15%">Modified By</th>
                            <th width="30%">Description</th>
                            <th width="20%">Comment</th>
                            <th width="15%">Activity Type</th>
                            <th width="10%">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {groupList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default Home;