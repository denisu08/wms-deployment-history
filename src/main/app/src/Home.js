import React, {Component} from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import moment from 'moment';
import AppNavbar from './AppNavbar';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

const FORMAT_DATE = 'YYYY-MM-DDTHH:mm';
const fileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const fileExtension = '.xlsx';

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            isLoading: false,
            uniqueFlag: true,
            startTime: moment().subtract(1, 'd').format(FORMAT_DATE),
            endTime: moment().format(FORMAT_DATE)
        };
        this.remove = this.remove.bind(this);
        this.onDownloadMeta = this.onDownloadMeta.bind(this);
        this.onSearchClick = this.onSearchClick.bind(this);
    }

    handleChange(evt) {
        if(evt.target.id === 'uniqueFlag') {
            this.setState({[evt.target.id]: evt.target.checked})
        } else {
            this.setState({[evt.target.id]: evt.target.value})
        }
    }

    onDownloadMeta(evt) {
        console.log('onDownloadMeta');
        const {groups : csvData} = this.state;
        const ws = XLSX.utils.json_to_sheet(csvData);
        const wb = { Sheets: { 'data': ws }, SheetNames: ['data'] };
        const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });
        const data = new Blob([excelBuffer], {type: fileType});
        FileSaver.saveAs(data, "downloadMeta" + fileExtension);
    }

    onSearchClick(evt) {
        const { startTime, endTime, uniqueFlag } = this.state;
        const startTimeMoment = moment(startTime, FORMAT_DATE);
        const endTimeMoment = moment(endTime, FORMAT_DATE);

        if(startTimeMoment.isBefore(endTimeMoment)) {
            this.setState({isLoading: true});
            if(this.getDaysRange(startTime, endTime) < 3) {
                fetch(`api/histories?startTime=${startTime}&endTime=${endTime}&uniqueFlag=${uniqueFlag}`)
                    .then(response => response.json())
                    .then(data => this.setState({groups: data, isLoading: false}));
            } else {
                alert("Error: Range no more than 2 days");
            }
        } else {
            alert("Error: Start Time is more than End Time");
        }
    }

    getDaysRange(date1, date2) {
        if (date1 && date2) {
            return Math.abs(moment(date2, FORMAT_DATE).diff(moment(date1, FORMAT_DATE), 'days')) + 1;
        }

        return undefined;
    };

    async remove(id) {
        await fetch(`/api/history/${id}`, {
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
        const {groups, isLoading, startTime, endTime, uniqueFlag} = this.state;

        let groupList = [];
        if(groups && groups.length > 0) {
            groupList = groups.map(group => {
                return <tr key={group._id}>
                    <td>{group.modifiedDate}</td>
                    <td>{group.modifiedBy}</td>
                    <td>{group.description}</td>
                    <td>{group.activityType}</td>
                    <td>{group.action}</td>
                </tr>
            });
        } else {
            groupList.push(
                (<tr>
                    <td colSpan="5" style={{textAlign:"center"}}>No Record</td>
                </tr>)
            );
        }

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <ButtonGroup>
                            <Button color="secondary" onClick={this.onDownloadMeta}>Download Meta</Button>
                        </ButtonGroup>
                    </div>
                    <h3>Deployment History</h3>
                    <hr/>
                    <div>
                        <form>
                            <div className="form-group row">
                                <label htmlFor="inputPassword" className="col-sm-2 col-form-label">Filter Date</label>
                                <div className="col-sm-3">
                                    <input type="datetime-local" className="form-control" id="startTime" placeholder="Start Time" value={startTime}
                                           onChange={(evt, value) => this.handleChange(evt, value)}></input>
                                </div>
                                <div className="col-sm-3">
                                    <input type="datetime-local" className="form-control" id="endTime" placeholder="End Time" value={endTime}
                                           onChange={(evt, value) => this.handleChange(evt, value)}></input>
                                </div>
                                <div className="col-sm-1.5 custom-control custom-checkbox">
                                    <input type="checkbox" id="uniqueFlag" className="custom-control-input" checked={uniqueFlag}
                                           onChange={(evt, value) => this.handleChange(evt, value)}></input>
                                    <label className="custom-control-label" htmlFor="uniqueFlag">Unique</label>
                                </div>
                                <div className="col-sm-1">
                                    <button type="button" className="btn btn-primary" onClick={this.onSearchClick} disabled={isLoading}>
                                        {isLoading ? <i className="fa fa-spinner fa-spin"></i>  : "Search"}
                                    </button>
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